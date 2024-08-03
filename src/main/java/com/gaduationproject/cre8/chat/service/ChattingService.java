package com.gaduationproject.cre8.chat.service;

import com.gaduationproject.cre8.chat.dto.request.ChatDto;
import com.gaduationproject.cre8.chat.dto.response.MessageResponseDto;
import com.gaduationproject.cre8.chat.entity.ChattingRoom;
import com.gaduationproject.cre8.chat.entity.Message;
import com.gaduationproject.cre8.chat.repository.ChattingRoomRepository;
import com.gaduationproject.cre8.chat.repository.MessageRepository;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final MemberRepository memberRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final MessagingService messagingService;
    private final MessageRepository messageRepository;


    public void sendMessage(final Long roomId,final ChatDto chatDto,final SimpMessageHeaderAccessor simpMessageHeaderAccessor){

        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_CHATTING_ROOM));
        Member sender = getCurrentLoginMember(simpMessageHeaderAccessor);

        checkCanPublishMessage(chattingRoom,sender);


        messagingService.sendMessage("/sub/chat/room/"+roomId,MessageResponseDto.ofPayLoad(sender.getId(),chatDto));

         messageRepository.save(Message.builder()
                .chattingRoom(chattingRoom)
                .sender(sender)
                .contents(chatDto.getMessage())
                .build());


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
