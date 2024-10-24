package com.gaduationproject.cre8.app.chat.service;

import com.gaduationproject.cre8.app.chat.dto.request.ChatDto;
import com.gaduationproject.cre8.app.chat.dto.response.MessageResponseDto;
import com.gaduationproject.cre8.app.notify.service.NotifyService;
import com.gaduationproject.cre8.externalApi.kafka.KafkaSender;
import com.gaduationproject.cre8.externalApi.mongodb.domain.ChattingMessage;
import com.gaduationproject.cre8.domain.chat.entity.ChattingRoom;
import com.gaduationproject.cre8.externalApi.mongodb.domain.NotificationType;
import com.gaduationproject.cre8.externalApi.mongodb.domain.Notify;
import com.gaduationproject.cre8.externalApi.mongodb.repository.ChattingMessageRepository;
import com.gaduationproject.cre8.domain.chat.repository.ChattingRoomRepository;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.externalApi.redis.service.ChattingRoomConnectService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingService {

    private final MemberRepository memberRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final MessagingService messagingService;
    //private final MessageRepository messageRepository;
    private final ChattingMessageRepository chattingMessageRepository;
    private final ChattingRoomConnectService chattingRoomConnectService;
    private final MongoTemplate mongoTemplate;
    private final KafkaSender kafkaSender;
    private static final String SUB = "SUB";



    public void sendMessage(final Long roomId,final ChatDto chatDto,final SimpMessageHeaderAccessor simpMessageHeaderAccessor){

        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_CHATTING_ROOM));
        Member sender = getCurrentLoginMember(simpMessageHeaderAccessor);

        checkCanPublishMessage(chattingRoom,sender);

        int readCount = chattingRoomConnectService.isAllConnected(chattingRoom.getId())?0:1;


        LocalDateTime messageCreatedTime = LocalDateTime.now();

        messagingService.sendMessage("/sub/chat/room/"+roomId,
                MessageResponseDto.ofPayLoad(sender.getId(),chatDto,messageCreatedTime,readCount,roomId));

      //  kafkaSender.send("chattest",MessageResponseDto.ofPayLoad(sender.getId(),chatDto,messageCreatedTime,readCount,roomId));


         chattingMessageRepository.save(ChattingMessage.builder()
                 .chattingRoomId(roomId)
                 .senderId(sender.getId())
                 .contents(chatDto.getMessage())
                 .createdAt(messageCreatedTime)
                 .readCount(readCount)
                 .build());


    }

    public void updateCountAllZero(Long chattingRoomId, String loginId) {

        Member findMember = memberRepository.findMemberByLoginId(loginId)
                .orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_MEMBER));

        Update update = new Update().set("readCount", 0);
        Query query = new Query(Criteria.where("chattingRoomId").is(chattingRoomId)
                .and("senderId").ne(findMember.getId()));

        mongoTemplate.updateMulti(query, update, ChattingMessage.class);
    }

    public MessageResponseDto sendEnterMessageAfterSubscribe(final Long chattingRoomId,
                                                             final String loginId,
                                                             final SimpMessageHeaderAccessor headerAccessor){

        chattingRoomConnectService.connectChattingRoom(chattingRoomId,loginId,headerAccessor.getSessionId());
        updateCountAllZero(chattingRoomId,loginId);

        headerAccessor.getSessionAttributes().put(SUB,chattingRoomId);


        log.info("채팅방 입장: chattingRoomId: {}",chattingRoomId);

        return MessageResponseDto.ofEnter("접속하였습니다"+loginId,chattingRoomId);

    }

    public void sendEnterMessage(Long chattingRoomId, String loginId) {

        messagingService.sendMessage("/sub/chat/room/"+chattingRoomId,MessageResponseDto.ofEnter("접속하였습니다"+loginId,chattingRoomId));

     //   kafkaSender.send("chattest",MessageResponseDto.ofEnter("접속하였습니다"+loginId,chattingRoomId));
    }



    private Member getCurrentLoginMember(final SimpMessageHeaderAccessor simpMessageHeaderAccessor){

        if(simpMessageHeaderAccessor.getUser()==null){
            throw new NotFoundException(ErrorCode.CANT_FIND_MEMBER);
        }

        return memberRepository.findMemberByLoginId(simpMessageHeaderAccessor.getUser().getName()).orElseThrow(
                ()->new NotFoundException(ErrorCode.CANT_FIND_MEMBER));
    }

    private void checkCanPublishMessage(ChattingRoom chattingRoom, Member sender){

        if(!chattingRoom.getSender().getId().equals(sender.getId()) &&
           !chattingRoom.getReceiver().getId().equals(sender.getId())){

            throw new BadRequestException(ErrorCode.PUB_URL_CANT_ACCESS);
        }
    }

}
