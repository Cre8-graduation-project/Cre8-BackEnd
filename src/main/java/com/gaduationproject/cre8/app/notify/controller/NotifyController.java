package com.gaduationproject.cre8.app.notify.controller;

import com.gaduationproject.cre8.app.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.app.notify.dto.response.NotifyDto;
import com.gaduationproject.cre8.app.notify.dto.response.NotifyExistDto;
import com.gaduationproject.cre8.app.notify.service.NotifyService;
import com.gaduationproject.cre8.app.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notify")
@Tag(name = "공지 관련 컨트롤러", description = "채팅, 답글 이 달렸을 때 공지를 드립니다")
public class NotifyController {

    private final NotifyService notifyService;

    @GetMapping("/check")
    @Operation(summary = "안읽은 공지가 있는지 확인",description = "채팅, 커뮤니티 답글 에서 안읽은 공지가 있다면 true 값을 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "채팅, 커뮤니티 답글 공지 유뮤 성공적 전송"),
    })
    public ResponseEntity<BaseResponse<NotifyExistDto>> checkUnReadNotify(@CurrentMemberLoginId final String loginId) {

       return  ResponseEntity.ok(BaseResponse.createSuccess(notifyService.checkUnReadNotify(loginId)));
    }


    @GetMapping
    @Operation(summary = "안읽은 공지 확인",description = "채팅을 제외한 안읽은 공지를 확인한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "채팅, 커뮤니티 답글 공지 유뮤 성공적 전송"),
    })
    public ResponseEntity<BaseResponse<List<NotifyDto>>> showUnReadNotify(@CurrentMemberLoginId final String loginId) {

        return  ResponseEntity.ok(BaseResponse.createSuccess(notifyService.showNonChattingNotifyList(loginId)));
    }

}
