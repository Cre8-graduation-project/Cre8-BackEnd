package com.gaduationproject.cre8.api.member.controller;

import com.gaduationproject.cre8.security.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.common.response.BaseResponse;
import com.gaduationproject.cre8.api.member.dto.ProfileWithUserInfoEditRequestDto;
import com.gaduationproject.cre8.api.member.dto.ProfileWithUserInfoResponseDto;
import com.gaduationproject.cre8.api.member.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "프로필 관련 용도 컨트롤러", description = "프로필과 관련된 활동을 모아두는 컨트롤러입니다.")
public class ProfileController {

    private final ProfileService profileService;



    @GetMapping(value = "/{memberId}/profile")
    @Operation(summary = "Profile 을 조회",description = "profile을 조회해볼 수 있습니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "성공적 반환")
    })
    public ResponseEntity<BaseResponse<ProfileWithUserInfoResponseDto>> showProfile(@PathVariable("memberId") final Long memberId){

        return ResponseEntity.ok(BaseResponse.createSuccess(profileService.showProfile(memberId)));
    }

    @PutMapping(value = "/profiles",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "자신의 Profile을 수정 및 입력",description = "자신의 profile을 수정 및 입력 해볼 수 있습니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "성공적 수정")
    })
    public ResponseEntity<Void> changeMyProfile(@CurrentMemberLoginId String loginId,@Valid @ModelAttribute
    ProfileWithUserInfoEditRequestDto profileWithUserInfoEditRequestDto){

        profileService.changeMemberProfile(loginId, profileWithUserInfoEditRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/members/pk")
    @Operation(summary = "유저 아이디를 통해 pk 를 가져옴",description = "유저의 아이디를 통해 pk 를 가져오는 util 적 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "성공적으로 pk 를 가져옴")
    })
    public ResponseEntity<BaseResponse<Long>> getPKFromLoginId(@RequestParam("loginId")final String loginId){

        return ResponseEntity.ok(BaseResponse.createSuccess(profileService.findPkFromNickName(loginId)));
    }






}
