package com.cre8.chat.service;


import com.cre8.chat.dto.request.ChatDto;
import com.cre8.chat.dto.response.MessageResponseDto;
import com.cre8.chat.entity.ChattingRoom;
import com.cre8.chat.repository.ChattingRoomRepository;
import com.cre8.member.entity.Member;
import com.cre8.member.repository.MemberRepository;
import com.cre8.mongodb.domain.ChattingMessage;
import com.cre8.mongodb.repository.ChattingMessageRepository;
import com.cre8.redis.pubsub.RedisPublisher;
import com.cre8.redis.service.ChattingRoomConnectService;
import com.cre8.response.error.ErrorCode;
import com.cre8.response.error.exception.BadRequestException;
import com.cre8.response.error.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

// [Before] InMemory Simple Broker 방식
// import com.cre8.chat.service.MessagingService;
// import com.cre8.kafka.KafkaSender;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingService {

    private final MemberRepository memberRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingMessageRepository chattingMessageRepository;
    private final ChattingRoomConnectService chattingRoomConnectService;
    private final MongoTemplate mongoTemplate;
    private final RedisPublisher redisPublisher;   // [After] Redis Pub/Sub
    private final ObjectMapper objectMapper;

    // [Before] InMemory Simple Broker
    private final MessagingService messagingService;
    // private final KafkaSender kafkaSender;

    private static final String SUB = "SUB";
    private static final String CHAT_ROOM_CHANNEL_PREFIX = "chat-room-";


    public void sendMessage(final Long roomId, final ChatDto chatDto, final SimpMessageHeaderAccessor simpMessageHeaderAccessor) {

        ChattingRoom chattingRoom = chattingRoomRepository.findById(1L).orElseThrow(()->new NotFoundException(
                ErrorCode.CANT_FIND_CHATTING_ROOM));
        Member sender = getCurrentLoginMember(simpMessageHeaderAccessor);

        checkCanPublishMessage(chattingRoom, sender);

        int readCount = chattingRoomConnectService.isAllConnected(chattingRoom.getId()) ? 0 : 1;

        LocalDateTime messageCreatedTime = LocalDateTime.now();

        MessageResponseDto messageResponseDto = MessageResponseDto.ofPayLoad(sender.getId(), chatDto, messageCreatedTime, readCount, roomId);

        // [Before] InMemory Simple Broker 방식
        // messagingService.sendMessage("/sub/chat/room/" + roomId, messageResponseDto);
        // kafkaSender.send("chattest", messageResponseDto);

        chattingMessageRepository.save(ChattingMessage.builder()
                .chattingRoomId(roomId)
                .senderId(sender.getId())
                .contents(chatDto.getMessage())
                .createdAt(messageCreatedTime)
                .readCount(readCount)
                .build());

        // [After] Redis Pub/Sub 방식: Redis 채널로 발행 → RedisSubscriber가 수신 후 STOMP로 브로드캐스트
        publishToRedis(CHAT_ROOM_CHANNEL_PREFIX + roomId, messageResponseDto);
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
                                                             final SimpMessageHeaderAccessor headerAccessor) {

        chattingRoomConnectService.connectChattingRoom(chattingRoomId, loginId, headerAccessor.getSessionId());
        updateCountAllZero(chattingRoomId, loginId);

        headerAccessor.getSessionAttributes().put(SUB, chattingRoomId);

        log.info("채팅방 입장: chattingRoomId: {}", chattingRoomId);

        return MessageResponseDto.ofEnter("접속하였습니다" + loginId, chattingRoomId);
    }

    public void sendEnterMessage(Long chattingRoomId, String loginId) {

        MessageResponseDto enterMessage = MessageResponseDto.ofEnter("접속하였습니다" + loginId, chattingRoomId);

       //  [Before] InMemory Simple Broker 방식
       // messagingService.sendMessage("/sub/chat/room/" + chattingRoomId, enterMessage);
        // kafkaSender.send("chattest", enterMessage);

        // [After] Redis Pub/Sub 방식
       publishToRedis(CHAT_ROOM_CHANNEL_PREFIX + chattingRoomId, enterMessage);
    }

    private void publishToRedis(String channel, MessageResponseDto messageResponseDto) {
        try {
            redisPublisher.publish(channel, objectMapper.writeValueAsString(messageResponseDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis 메시지 직렬화 실패", e);
        }
    }

    private Member getCurrentLoginMember(final SimpMessageHeaderAccessor simpMessageHeaderAccessor) {

        if (simpMessageHeaderAccessor.getUser() == null) {
            throw new NotFoundException(ErrorCode.CANT_FIND_MEMBER);
        }

        return memberRepository.findMemberByLoginId(simpMessageHeaderAccessor.getUser().getName()).orElseThrow(
                ()->new NotFoundException(ErrorCode.CANT_FIND_MEMBER));
    }

    private void checkCanPublishMessage(ChattingRoom chattingRoom, Member sender) {

        if (!chattingRoom.getSender().getId().equals(sender.getId()) &&
            !chattingRoom.getReceiver().getId().equals(sender.getId())) {

            throw new BadRequestException(ErrorCode.PUB_URL_CANT_ACCESS);
        }
    }
}
