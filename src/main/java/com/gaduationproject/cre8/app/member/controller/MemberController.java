package com.gaduationproject.cre8.app.member.controller;

import com.gaduationproject.cre8.app.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.app.response.BaseResponse;
import com.gaduationproject.cre8.app.member.dto.LoginIdCheckResponseDto;
import com.gaduationproject.cre8.app.member.dto.MemberSignUpRequestDto;
import com.gaduationproject.cre8.app.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "사용자 관련 용도 컨트롤러", description = "사용자의 구체적인 회원가입 과정에서 사용되는 활동을 모아두는 컨트롤러입니다.")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "회원가입을 위한 최종 api",description = "마지막으로 아이디,닉네임,이메일에 대해 중복되는지 체크하고 회원가입을 진행 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "회원가입 성공"),
            @ApiResponse(responseCode = "400",description = "아이디 or 닉네임 or 이메일이 중복되는 경우")
    })
    public ResponseEntity<Void> saveMember(@Valid @RequestBody final MemberSignUpRequestDto memberSignUpRequestDto){
        memberService.saveMember(memberSignUpRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/check")
    @Operation(summary = "중복 되는 로그인 아이디가 있는지 체크 api",description = "중복되는 로그인 아이디가 있는지 확인합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "중복되는 로그인 아이디가 없는 경우"),
            @ApiResponse(responseCode = "400",description = "중복되는 로그인 아이디가 있는 경우")
    })
    public ResponseEntity<BaseResponse<LoginIdCheckResponseDto>> checkExistsLoginId(@RequestParam("login-id") final
            String loginId){

        return ResponseEntity.ok(BaseResponse.createSuccess(memberService.checkExistsLoginId(loginId)));
    }

    @DeleteMapping
    @Operation(summary = "회원 탈퇴",description = "회원 탈퇴 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "성공적 회원 탈퇴 "),
    })
    public ResponseEntity<Void> deleteMember(@CurrentMemberLoginId final String loginId){

        memberService.deleteMember(loginId);

        return ResponseEntity.ok().build();
    }

}
