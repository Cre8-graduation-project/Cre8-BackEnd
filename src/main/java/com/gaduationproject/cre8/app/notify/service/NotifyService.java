package com.gaduationproject.cre8.app.notify.service;

import com.gaduationproject.cre8.app.notify.dto.response.NotifyDto;
import com.gaduationproject.cre8.app.notify.dto.response.NotifyExistDto;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.chat.entity.ChattingRoom;
import com.gaduationproject.cre8.domain.chat.repository.ChattingRoomRepository;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.externalApi.mongodb.domain.ChattingMessage;
import com.gaduationproject.cre8.externalApi.mongodb.domain.Notify;
import com.gaduationproject.cre8.externalApi.mongodb.repository.NotifyRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotifyService {

    private final NotifyRepository notifyRepository;
    private final MemberRepository memberRepository;
    private final MongoTemplate mongoTemplate;
    private final ChattingRoomRepository chattingRoomRepository;


    public void saveNotify(Notify notify){

        notifyRepository.save(notify);

    }

    public NotifyExistDto checkUnReadNotify(final String loginId){

        Member member = getCurrentLoginMember(loginId);

        return NotifyExistDto.of(
                haveUnReadMessage(member.getId()),
                notifyRepository.existsByMemberIdAndAndRead(member.getId(),false)
                );

    }

    public List<NotifyDto> showNonChattingNotifyList(final String loginId){

        Member member = getCurrentLoginMember(loginId);

        List<Notify> unReadNotifyList = notifyRepository.findByMemberIdAndRead(member.getId(),false);

        readNotify(member);

        return unReadNotifyList.stream().map(NotifyDto::from).collect(Collectors.toList());


    }

    private void readNotify(Member member) {

        Update update = new Update().set("read",true);
        Query query = new Query(Criteria.where("memberId").is(member.getId())
                               .and("read").is(false));

        mongoTemplate.updateMulti(query,update,Notify.class);
    }


    private Member getCurrentLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()-> new NotFoundException(
                ErrorCode.CANT_FIND_MEMBER));

    }


    private  boolean haveUnReadMessage(Long senderId) {

        List<Long> chattingRoomIds = chattingRoomRepository.findByBelongChattingRoom(senderId)
                .stream()
                .map(ChattingRoom::getId)
                .collect(Collectors.toList());

        Query query = new Query(Criteria.where("chattingRoomId").in(chattingRoomIds)
                .and("readCount").is(1)
                .and("senderId").ne(senderId));

        return mongoTemplate.exists(query, ChattingMessage.class);
    }

}
