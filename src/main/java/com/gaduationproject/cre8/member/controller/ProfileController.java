package com.gaduationproject.cre8.member.controller;

import com.gaduationproject.cre8.auth.interfaces.CurrentMember;
import com.gaduationproject.cre8.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.auth.service.CustomUserDetails;
import com.gaduationproject.cre8.common.response.BaseResponse;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.member.dto.ProfileEditRequestDto;
import com.gaduationproject.cre8.member.dto.ProfileResponseDto;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.repository.MemberRepository;
import com.gaduationproject.cre8.member.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
@Tag(name = "프로필 관련 용도 컨트롤러", description = "프로필과 관련된 활동을 모아두는 컨트롤러입니다.")
public class ProfileController {

    private final ProfileService profileService;



    @GetMapping
    @Operation(summary = "자신의 Profile을 조회",description = "자신의 profile을 조회해볼 수 있습니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "성공적 반환")
    })
    public ResponseEntity<BaseResponse<ProfileResponseDto>> showProfile(@CurrentMemberLoginId String loginId){

        return ResponseEntity.ok(BaseResponse.createSuccess(profileService.showMyProfile(loginId)));
    }

    @PutMapping
    @Operation(summary = "자신의 Profile을 수정 및 입력",description = "자신의 profile을 수정 및 입력 해볼 수 있습니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "성공적 수정")
    })
    public ResponseEntity<Void> changeMyProfile(@CurrentMemberLoginId String loginId,@RequestBody
            ProfileEditRequestDto profileEditRequestDto){

        profileService.changeMemberProfile(loginId,profileEditRequestDto);
        return ResponseEntity.ok().build();
    }





}
