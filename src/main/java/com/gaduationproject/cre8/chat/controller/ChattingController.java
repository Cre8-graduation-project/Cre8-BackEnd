package com.gaduationproject.cre8.chat.controller;

import com.gaduationproject.cre8.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.chat.dto.response.MessageResponseDto;
import com.gaduationproject.cre8.chat.service.ChattingRoomService;
import com.gaduationproject.cre8.common.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChattingController {

    private final ChattingRoomService chattingRoomService;


    @GetMapping
    public ResponseEntity<BaseResponse<List<MessageResponseDto>>> showChatList(@CurrentMemberLoginId String loginId,
                                                 final Long opponentId){

        return ResponseEntity.ok(BaseResponse.createSuccess(chattingRoomService.showChattingList(opponentId,loginId)));

    }
}
