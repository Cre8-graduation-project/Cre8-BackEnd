package com.gaduationproject.cre8.chat.controller;

import com.gaduationproject.cre8.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.auth.service.CustomUserDetails;
import com.gaduationproject.cre8.chat.dto.request.ChatDto;
import com.gaduationproject.cre8.chat.dto.response.MessageResponseDto;
import com.gaduationproject.cre8.chat.service.ChattingRoomService;
import com.gaduationproject.cre8.chat.service.ChattingService;
import com.gaduationproject.cre8.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "채팅 관련 용도 컨트롤러", description = "채팅과 관련된 활동을 모아두는 컨트롤러입니다.")
@RequestMapping("/api/v1/chats")
public class ChattingController {

    private final ChattingRoomService chattingRoomService;
    private final ChattingService chattingService;


    @GetMapping
    @Operation(summary = "상대방과의 채팅 내역 조회",description = "상대방과의 채팅 기록이 존재시 상대방과의 채팅 내역을 조회할 수 있습니다. 그렇지 않으면 "
            + "새로운 채팅방을 생성하면서 빈 값 [] 이 반환됩니다")
    public ResponseEntity<BaseResponse<List<MessageResponseDto>>> showChatList(@CurrentMemberLoginId String loginId,
                                                 final Long opponentId){

        return ResponseEntity.ok(BaseResponse.createSuccess(chattingRoomService.showChattingList(opponentId,loginId)));

    }

    @MessageMapping("/message/{roomId}")
    public ResponseEntity<Void> sendMessage(@DestinationVariable("roomId")final Long roomId, @Payload  @Valid final ChatDto chatDto,
            final SimpMessageHeaderAccessor simpMessageHeaderAccessor){

        chattingService.sendMessage(roomId,chatDto,simpMessageHeaderAccessor);

        return ResponseEntity.ok().build();
    }


}
