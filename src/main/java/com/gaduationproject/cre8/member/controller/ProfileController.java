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
public class ProfileController {

    private final ProfileService profileService;



    @GetMapping
    public ResponseEntity<BaseResponse<ProfileResponseDto>> showProfile(@CurrentMemberLoginId String loginId){

        return ResponseEntity.ok(BaseResponse.createSuccess(profileService.showMyProfile(loginId)));
    }

    @PutMapping
    public ResponseEntity<Void> changeMyProfile(@CurrentMemberLoginId String loginId,@RequestBody
            ProfileEditRequestDto profileEditRequestDto){

        profileService.changeMemberProfile(loginId,profileEditRequestDto);
        return ResponseEntity.ok().build();
    }





}
