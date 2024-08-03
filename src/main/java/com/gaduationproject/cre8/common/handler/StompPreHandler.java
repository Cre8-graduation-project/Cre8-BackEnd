package com.gaduationproject.cre8.common.handler;

import com.gaduationproject.cre8.auth.jwt.TokenProvider;
import com.gaduationproject.cre8.auth.service.CustomUserDetails;
import com.gaduationproject.cre8.chat.entity.ChattingRoom;
import com.gaduationproject.cre8.chat.repository.ChattingRoomRepository;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;


@Configuration
@RequiredArgsConstructor
public class StompPreHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private final ChattingRoomRepository chattingRoomRepository;
    private static final String CHAT_SUB_PREFIX = "/sub/chat/room";
    private static final String CHAT_SUB_ERROR_PREFIX="/user/queue/error";

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
            // destination을 사용하여 구독 경로를 확인하거나 로깅
            System.out.println("Subscribing to destination: " + destination);

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




}
