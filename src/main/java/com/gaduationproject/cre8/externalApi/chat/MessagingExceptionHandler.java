package com.gaduationproject.cre8.externalApi.chat;


import com.gaduationproject.cre8.api.chat.dto.request.ChatDto;
import com.gaduationproject.cre8.externalApi.chat.StompErrorHandler;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@RequiredArgsConstructor
public class MessagingExceptionHandler {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final StompErrorHandler stompErrorHandler;


    @MessageExceptionHandler
    @SendToUser("/queue/error")
    public ChatDto handleMethodArgNotValidException(
            org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException exception){

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

       ChatDto chatDto = ChatDto.builder()
               .message(builder.toString())
               .build();


        return chatDto;


    }

    @MessageExceptionHandler(NotFoundException.class)
    @SendToUser("/queue/error")
    public ChatDto handleNotFoundException(
            NotFoundException exception){

        ChatDto chatDto = ChatDto.builder().message(exception.getMessage()).build();

        return chatDto;

    }

    @MessageExceptionHandler(BadRequestException.class)
    @SendToUser("/queue/error")
    public ChatDto handleBadRequestException(
            BadRequestException exception,
            StompHeaderAccessor stompHeaderAccessor, Principal principal){

        ChatDto chatDto = ChatDto.builder().message(exception.getMessage()).build();

        return chatDto;

    }

}
