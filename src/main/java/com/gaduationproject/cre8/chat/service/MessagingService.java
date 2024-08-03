package com.gaduationproject.cre8.chat.service;

import com.gaduationproject.cre8.chat.dto.request.ChatDto;
import com.gaduationproject.cre8.chat.dto.response.MessageResponseDto;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageDeliveryException;
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
