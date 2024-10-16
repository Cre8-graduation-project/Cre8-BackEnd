package com.gaduationproject.cre8.app.member.controller;

import com.gaduationproject.cre8.app.member.dto.LoginIdRequestDto;
import com.gaduationproject.cre8.app.response.BaseResponse;
import com.gaduationproject.cre8.app.member.dto.EmailCheckAuthNumRequestDto;
import com.gaduationproject.cre8.app.member.dto.EmailCheckAuthNumResponseDto;
import com.gaduationproject.cre8.app.member.dto.EmailRequestDto;
import com.gaduationproject.cre8.app.member.service.MemberMailSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
@Tag(name = "메일 관련 용도 컨트롤러", description = "메일과 관련된 활동을 모아두는 컨트롤러입니다.")
public class MailController {

    private final MemberMailSendService mailService;


    @PostMapping
    @Operation(summary = "이메일에 인증번호 전송",description = "사용자의 이메일을 받으면 중복 이메일을 체크하고 이메일을 전송합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "이메일에 성공적으로 인증번호 전송"),
            @ApiResponse(responseCode = "400",description = "이메일이 중복"),
            @ApiResponse(responseCode = "500",description = "현재 이메일 서버 상태 문제")
    })
    public void mailSend(@RequestBody @Valid final EmailRequestDto emailDto) {

         mailService.sendMail(emailDto);
    }

    @PostMapping("/check")
    @Operation(summary = "이메일에 대한 인증번호가 맞는지 확인",description = "사용자의 인증번호를 받아서 이메일에 대한 인증번호가 맏는지 확인합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "이메일에 대한 인증번호가 맞는 경우"),
            @ApiResponse(responseCode = "400",description = "이메일에 대한 인증번호가 맞지 않는 경우")
    })
    public ResponseEntity<BaseResponse<EmailCheckAuthNumResponseDto>> AuthCheck(@RequestBody @Valid final EmailCheckAuthNumRequestDto emailCheckAuthNumRequestDto){

        return ResponseEntity.ok(BaseResponse.createSuccess(mailService.checkAuthNum(
                emailCheckAuthNumRequestDto)));
    }

    @PostMapping("/temp/password")
    @Operation(summary = "새로운 임시 비밀번호를 이메일에 전송",description = "새로운 임시 비밀번호를 이메일에 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "새로운 임시 비밀번호 가입 당시 이메일로 성공적 전송"),
            @ApiResponse(responseCode = "400",description = "로그인 아이디를 찾을 수 없는 경우")
    })
    public ResponseEntity<Void> sendTMPPassword(@RequestBody @Valid final LoginIdRequestDto loginIdRequestDto){

        mailService.sendTMPPassword(loginIdRequestDto);

        return ResponseEntity.ok().build();
    }

}