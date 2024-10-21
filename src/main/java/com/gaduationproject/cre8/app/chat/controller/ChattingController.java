package com.gaduationproject.cre8.app.chat.controller;

import com.gaduationproject.cre8.app.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.app.chat.dto.request.ChatDto;
import com.gaduationproject.cre8.app.chat.dto.response.ChattingRoomInfoResponseDto;
import com.gaduationproject.cre8.app.chat.dto.response.ChattingRoomResponseDto;
import com.gaduationproject.cre8.app.chat.dto.response.MessageResponseDto;
import com.gaduationproject.cre8.app.chat.service.ChattingRoomService;
import com.gaduationproject.cre8.app.chat.service.ChattingService;
import com.gaduationproject.cre8.app.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "채팅 관련 용도 컨트롤러", description = "채팅과 관련된 활동을 모아두는 컨트롤러입니다.")
@RequestMapping("/api/v1/chats")
public class ChattingController {

    private final ChattingRoomService chattingRoomService;
    private final ChattingService chattingService;


    @GetMapping("/user/{opponentId}")
    @Operation(summary = "상대방과의 채팅방 조회",description = "상대방과의 채팅 기록이 존재시 그 채팅방의 아이디를, "
            + "처음 채팅시 그 채팅방을 새로 생성후 그 아이디를 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "member 의 loginId 를 기반으로 조회해 오는 것을 실패")
    })
    public ResponseEntity<BaseResponse<Long>> showChatListByOpponentId(@CurrentMemberLoginId String loginId,
                                                                               @PathVariable(name = "opponentId") final Long opponentId){

        return ResponseEntity.ok(BaseResponse.createSuccess(chattingRoomService.getChattingRoomNumberByOpponentId(opponentId,loginId)));

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
    @Operation(summary = "채팅방 채팅리스트, 방 정보 조회",description = "채팅방의 ID 로 채팅 리스트, 채팅방 정보를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500",description = "filter 가 동작하지 않았을 경우 이므로 500 반환시 알려주십시오"),
            @ApiResponse(responseCode = "404",description = "member 의 loginId 를 기반으로 조회해 오는 것을 실패 or roomId 를 기반으로 채팅방 조회 실패"),
            @ApiResponse(responseCode = "400",description = "자신이 속해 있지 않은 채팅방의 채팅 리스트에 접근 하려 할때 ")
    })
    public ResponseEntity<BaseResponse<ChattingRoomInfoResponseDto>> showChatListWithRoomInfoByChattingRoomId(@CurrentMemberLoginId final String loginId,
                                                                                               @PathVariable("roomId")final Long roomId,
                                                                                               @PageableDefault(size = 20,sort = "createdAt",direction = Direction.DESC,page = 0) final Pageable pageable){

        return ResponseEntity.ok(BaseResponse.createSuccess(chattingRoomService.showChattingListWithRoomInfoByChattingRoomId(roomId,loginId,pageable)));


    }




    @MessageMapping("/message/{roomId}")
    public ResponseEntity<Void> sendMessage(@DestinationVariable("roomId")final Long roomId, @Payload  @Valid final ChatDto chatDto,
            final SimpMessageHeaderAccessor simpMessageHeaderAccessor){

        chattingService.sendMessage(roomId,chatDto,simpMessageHeaderAccessor);

        return ResponseEntity.ok().build();
    }

//    @SubscribeMapping("/chat/room/{roomId}")
//    public MessageResponseDto afterSubscribe(@DestinationVariable("roomId") final Long roomId, final SimpMessageHeaderAccessor simpMessageHeaderAccessor){
//
//        return chattingService.sendEnterMessageAfterSubscribe(roomId,simpMessageHeaderAccessor.getUser().getName(),
//                                                               simpMessageHeaderAccessor);
//
//    }

//    @MessageMapping("message.{roomId}")
//    public ResponseEntity<Void> sendMessage2(@DestinationVariable("roomId")final Long roomId, @Payload  @Valid final ChatDto chatDto,
//            final SimpMessageHeaderAccessor simpMessageHeaderAccessor) throws InterruptedException {
//
//        System.out.println("여기 안오지?");
//        chattingService.sendMessage(roomId,chatDto,simpMessageHeaderAccessor);
//
//        return ResponseEntity.ok().build();
//    }



}
