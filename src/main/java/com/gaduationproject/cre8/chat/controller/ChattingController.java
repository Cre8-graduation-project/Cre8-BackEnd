package com.gaduationproject.cre8.chat.controller;

import com.amazonaws.Response;
import com.gaduationproject.cre8.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.auth.service.CustomUserDetails;
import com.gaduationproject.cre8.chat.dto.request.ChatDto;
import com.gaduationproject.cre8.chat.dto.response.ChattingRoomResponseDto;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "채팅 관련 용도 컨트롤러", description = "채팅과 관련된 활동을 모아두는 컨트롤러입니다.")
@RequestMapping("/api/v1/chats")
public class ChattingController {

    private final ChattingRoomService chattingRoomService;
    private final ChattingService chattingService;


    @GetMapping("/user/{opponentId}")
    @Operation(summary = "상대방과의 채팅 내역 조회",description = "상대방과의 채팅 기록이 존재시 상대방과의 채팅 내역을 조회할 수 있습니다. 그렇지 않으면 "
            + "새로운 채팅방을 생성하면서 빈 값 [] 이 반환됩니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "member 의 loginId 를 기반으로 조회해 오는 것을 실패")
    })
    public ResponseEntity<BaseResponse<List<MessageResponseDto>>> showChatListByOpponentId(@CurrentMemberLoginId String loginId,
                                                                               @PathVariable(name = "opponentId") final Long opponentId){

        return ResponseEntity.ok(BaseResponse.createSuccess(chattingRoomService.showChattingListByOpponentId(opponentId,loginId)));

    }

    @GetMapping("/room")
    @Operation(summary = "채팅방 리스트 조회",description = "채팅방의 리스트를 상대 사용자의 닉네임, 최근 메시지와 함께 조회 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500",description = "filter 가 동작하지 않았을 경우 이므로 500 반환시 알려주십시오"),
            @ApiResponse(responseCode = "404",description = "member 의 loginId 를 기반으로 조회해 오는 것을 실패")
    })
    public ResponseEntity<BaseResponse<List<ChattingRoomResponseDto>>> showChatRoomList(@CurrentMemberLoginId String loginId){

        return ResponseEntity.ok(BaseResponse.createSuccess(chattingRoomService.showChattingRoomList(loginId)));

    }

    @GetMapping("/room/{roomId}")
    @Operation(summary = "채팅 리스트 조회",description = "채팅방의 ID 로 채팅 리스트를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500",description = "filter 가 동작하지 않았을 경우 이므로 500 반환시 알려주십시오"),
            @ApiResponse(responseCode = "404",description = "member 의 loginId 를 기반으로 조회해 오는 것을 실패 or roomId 를 기반으로 채팅방 조회 실패"),
            @ApiResponse(responseCode = "400",description = "자신이 속해 있지 않은 채팅방의 채팅 리스트에 접근 하려 할때 ")
    })
    public ResponseEntity<BaseResponse<List<MessageResponseDto>>> showChatListByChattingRoomId(@CurrentMemberLoginId final String loginId,
                                                                                               @PathVariable("roomId")final Long roomId){

        return ResponseEntity.ok(BaseResponse.createSuccess(chattingRoomService.showChattingListByChattingRoomId(roomId,loginId)));


    }




    @MessageMapping("/message/{roomId}")
    public ResponseEntity<Void> sendMessage(@DestinationVariable("roomId")final Long roomId, @Payload  @Valid final ChatDto chatDto,
            final SimpMessageHeaderAccessor simpMessageHeaderAccessor){

        chattingService.sendMessage(roomId,chatDto,simpMessageHeaderAccessor);

        return ResponseEntity.ok().build();
    }


}
