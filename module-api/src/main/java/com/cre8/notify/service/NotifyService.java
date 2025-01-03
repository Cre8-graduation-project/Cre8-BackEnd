package com.cre8.notify.service;


import com.cre8.chat.entity.ChattingRoom;
import com.cre8.chat.repository.ChattingRoomRepository;
import com.cre8.member.entity.Member;
import com.cre8.member.repository.MemberRepository;
import com.cre8.mongodb.domain.ChattingMessage;
import com.cre8.mongodb.domain.Notify;
import com.cre8.mongodb.repository.NotifyRepository;
import com.cre8.notify.dto.response.NotifyDto;
import com.cre8.notify.dto.response.NotifyExistDto;
import com.cre8.response.error.ErrorCode;
import com.cre8.response.error.exception.NotFoundException;
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
