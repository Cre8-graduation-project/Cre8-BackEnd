package com.cre8.chat.service;


import com.cre8.chat.dto.response.ChattingRoomInfoResponseDto;
import com.cre8.chat.dto.response.ChattingRoomResponseDto;
import com.cre8.chat.dto.response.MessageResponseDto;
import com.cre8.chat.entity.ChattingRoom;
import com.cre8.chat.repository.ChattingRoomRepository;
import com.cre8.member.entity.Member;
import com.cre8.member.repository.MemberRepository;
import com.cre8.mongodb.domain.ChattingMessage;
import com.cre8.mongodb.repository.ChattingMessageRepository;
import com.cre8.response.error.ErrorCode;
import com.cre8.response.error.exception.BadRequestException;
import com.cre8.response.error.exception.InternalServerErrorException;
import com.cre8.response.error.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChattingRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final MemberRepository memberRepository;
    //private final MessageRepository messageRepository;
    private final ChattingMessageRepository chattingMessageRepository;
    private final MongoTemplate mongoTemplate;
    @Transactional
    public Long getChattingRoomNumberByOpponentId(final Long opponentId,final String loginId){


        ChattingRoom chattingRoom = getChattingRoomByParticipant(opponentId,loginId);

        return chattingRoom.getId();

    }

    public ChattingRoomInfoResponseDto showChattingListWithRoomInfoByChattingRoomId(final Long chattingRoomId, final String loginId,
            final Pageable pageable){

        Member currentMember = getCurrentLoginMember(loginId);

        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_CHATTING_ROOM));

        if(!chattingRoom.getReceiver().getId().equals(currentMember.getId())&&
           !chattingRoom.getSender().getId().equals(currentMember.getId())){

            throw new BadRequestException(ErrorCode.CANT_ACCESS_CHAT_ROOM);
        }

        //채팅 메시지
        Slice<ChattingMessage> chattingMessageSlice = chattingMessageRepository.findByChattingRoomId(
                chattingRoom.getId(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                        Sort.by("createdAt").descending()));


        return ChattingRoomInfoResponseDto.of(chattingMessageSlice.stream().map(MessageResponseDto::ofChatMessage).collect(
                Collectors.toList()), chattingMessageSlice.hasNext());


    }



    public List<ChattingRoomResponseDto> showChattingRoomList(final String loginId){

        Member currentMember = getCurrentLoginMember(loginId);

        List<ChattingRoom> chattingRoomList = chattingRoomRepository.findByBelongChattingRoom(currentMember.getId());


        return chattingRoomList.stream().filter(chattingRoom -> chattingMessageRepository.
                        findTop1ByChattingRoomIdOrderByCreatedAtDesc(chattingRoom.getId()).isPresent())
                        .map(chattingRoom -> {

                            Member opponent = chattingRoom.getSender().getId()== currentMember.getId()
                                    ?chattingRoom.getReceiver()
                                    :chattingRoom.getSender();

                            ChattingMessage message = chattingMessageRepository.findTop1ByChattingRoomIdOrderByCreatedAtDesc(chattingRoom.getId())
                                    .orElseThrow(()->new InternalServerErrorException(ErrorCode.NOT_APPLY_RECENT_CHAT_FILTER));

                            return ChattingRoomResponseDto.builder()
                                    .roomId(chattingRoom.getId())
                                    .latestMessage(message.getContents())
                                    .nickName(opponent.getNickName())
                                    .accessUrl(opponent.getAccessUrl())
                                    .unReadMessage(countUnReadMessages(chattingRoom.getId(),currentMember.getId()))
                                    .build();

                        }).collect(Collectors.toList());

    }



    private ChattingRoom getChattingRoomByParticipant(final Long opponentId, final String loginId){

        Member loginMember = getCurrentLoginMember(loginId);

        return chattingRoomRepository.findByParticipant(opponentId,loginMember.getId()).orElseGet(
                ()-> {

                    ChattingRoom newChattingRoom = ChattingRoom.builder()
                            .sender(loginMember)
                            .receiver(getMemberById(opponentId))
                            .build();

                    chattingRoomRepository.save(newChattingRoom);

                    return newChattingRoom;
                }
        );
    }

    private long countUnReadMessages(Long chattingRoomId, Long senderId) {

        Query query = new Query(Criteria.where("chattingRoomId").is(chattingRoomId)
                .and("readCount").is(1)
                .and("senderId").ne(senderId));

        return  mongoTemplate.count(query, ChattingMessage.class);
    }




    private Member getCurrentLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_MEMBER));
    }

    private Member getMemberById(final Long memberId){

        return memberRepository.findById(memberId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_MEMBER));
    }
}
