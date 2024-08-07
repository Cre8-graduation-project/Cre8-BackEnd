package com.gaduationproject.cre8.externalApi.chat;

import com.gaduationproject.cre8.api.chat.dto.response.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingService {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public void sendMessage(final String destination, final MessageResponseDto messageResponseDto){

        simpMessageSendingOperations.convertAndSend(destination,messageResponseDto);

    }

}
