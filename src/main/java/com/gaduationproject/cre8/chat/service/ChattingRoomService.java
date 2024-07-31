package com.gaduationproject.cre8.chat.service;

import com.gaduationproject.cre8.chat.dto.response.MessageResponseDto;
import com.gaduationproject.cre8.chat.entity.ChattingRoom;
import com.gaduationproject.cre8.chat.repository.ChattingRoomRepository;
import com.gaduationproject.cre8.chat.repository.MessageRepository;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChattingRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    @Transactional
    public List<MessageResponseDto> showChattingList(final Long opponentId,final String loginId){

        ChattingRoom chattingRoom = getChattingRoomByParticipant(opponentId,loginId);

        return messageRepository.findByChattingRoom(chattingRoom)
                .stream()
                .map(MessageResponseDto::of)
                .collect(Collectors.toList());

    }



    private ChattingRoom getChattingRoomByParticipant(final Long opponentId, final String loginId){

        Member loginMember = getCurrentLoginMember(loginId);

        return chattingRoomRepository.findByParticipant(opponentId,loginMember.getId()).orElseGet(
                ()-> {

                    System.out.println("여기 실행이 되니?");
                    ChattingRoom newChattingRoom = ChattingRoom.builder()
                            .sender(loginMember)
                            .receiver(getMemberById(opponentId))
                            .build();

                    chattingRoomRepository.save(newChattingRoom);

                    return newChattingRoom;
                }
        );
    }



    private Member getCurrentLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_MEMBER));
    }

    private Member getMemberById(final Long memberId){

        return memberRepository.findById(memberId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_MEMBER));
    }
}
