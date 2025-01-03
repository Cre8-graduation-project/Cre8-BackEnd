package com.cre8.chat.service;

import com.cre8.chat.dto.response.MessageResponseDto;
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
