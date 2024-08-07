package com.gaduationproject.cre8.externalApi.chat;


import com.gaduationproject.cre8.common.response.error.ErrorCode;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@RequiredArgsConstructor
@Configuration
public class StompErrorHandler extends StompSubProtocolErrorHandler {



    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage,
            Throwable ex) {

        System.out.println("StompErrorHandler.handleClientMessageProcessingError");
        System.out.println(ex.getMessage());
        System.out.println(ex.getCause());
        System.out.println(ex.getCause().getMessage());
        System.out.println(ex.getLocalizedMessage());
        System.out.println(ex.getSuppressed());
        System.out.println(ex.getStackTrace());


        if(ex.getCause().getMessage().equals(ErrorCode.ACCESS_TOKEN_NOT_MATCH.getMessage())) {
            return errorMessage(ErrorCode.ACCESS_TOKEN_NOT_MATCH);
        }

        if(ex.getCause().getMessage().equals(ErrorCode.SUB_URL_CANT_ACCESS.getMessage())){
            return errorMessage(ErrorCode.SUB_URL_CANT_ACCESS);
        }

        if(ex.getCause().getMessage().equals(ErrorCode.CANT_FIND_CHATTING_ROOM.getMessage())){
            return errorMessage(ErrorCode.CANT_FIND_CHATTING_ROOM);
        }

        if(ex.getCause().getMessage().equals(ErrorCode.SUB_URL_NOT_MATCH.getMessage())){
            return errorMessage(ErrorCode.SUB_URL_NOT_MATCH);
        }


        return super.handleClientMessageProcessingError(clientMessage, ex);


    }


    private Message<byte[]> errorMessage(ErrorCode errorCode) {
        String code = errorCode.getMessage();

        System.out.println("code"+code);
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setMessage("에러");
        accessor.setLeaveMutable(true);

        Message<byte[]> message = MessageBuilder.createMessage(
                code.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
        System.out.println(message);

        return message;
    }




}
