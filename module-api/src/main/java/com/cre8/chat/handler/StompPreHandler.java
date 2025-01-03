package com.cre8.chat.handler;


import com.cre8.auth.jwt.TokenProvider;
import com.cre8.chat.entity.ChattingRoom;
import com.cre8.chat.event.SessionSubscribedEvent;
import com.cre8.chat.repository.ChattingRoomRepository;
import com.cre8.response.error.ErrorCode;
import com.cre8.response.error.exception.BadRequestException;
import com.cre8.response.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.broker.AbstractBrokerMessageHandler;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;


@Configuration
@RequiredArgsConstructor
public class StompPreHandler implements ExecutorChannelInterceptor {

    private final TokenProvider tokenProvider;
    private final ChattingRoomRepository chattingRoomRepository;
    private static final String CHAT_SUB_PREFIX = "/sub/chat/room";
    private static final String CHAT_SUB_ERROR_PREFIX="/user/queue/error";

    private static final String CHAT_SUB_PREFIX_RABBIT = "/exchange/chat.exchange/room";

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        //헤더 접근
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {

            String accessToken = accessor.getFirstNativeHeader("Authorization");
            tokenProvider.validateToken(accessToken);
            Authentication authentication = tokenProvider.getAuthentication(accessToken);


            if (authentication != null) {

                accessor.setUser(authentication);
                return message;
            }

            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();


            if(!destination.equals(CHAT_SUB_ERROR_PREFIX)&& !destination.startsWith(CHAT_SUB_PREFIX)){
                throw new BadRequestException(ErrorCode.SUB_URL_NOT_MATCH);
            }

            if(destination.startsWith(CHAT_SUB_PREFIX)){
                Long roomId = Long.valueOf(destination.substring(CHAT_SUB_PREFIX.length()+1));
                ChattingRoom chattingRoom =
                        chattingRoomRepository.findById(roomId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_CHATTING_ROOM));

                if(!chattingRoom.getReceiver().getLoginId().equals(accessor.getUser().getName())&&
                !chattingRoom.getSender().getLoginId().equals(accessor.getUser().getName())){
                    throw new BadRequestException(ErrorCode.SUB_URL_CANT_ACCESS);
                }

            }


        }


        return message;
    }

    @Override
    public void afterMessageHandled(Message<?> message, MessageChannel channel, MessageHandler handler, Exception ex) {

        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(message);

        if (accessor.getMessageType() == SimpMessageType.SUBSCRIBE && handler instanceof AbstractBrokerMessageHandler) {

            applicationEventPublisher.publishEvent(new SessionSubscribedEvent(message));
        }
    }






}
