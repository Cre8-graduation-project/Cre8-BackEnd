package com.gaduationproject.cre8.member.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.common.s3.S3ImageService;
import com.gaduationproject.cre8.member.dto.ProfileWithUserInfoEditRequestDto;

import com.gaduationproject.cre8.member.dto.ProfileWithUserInfoResponseDto;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.entity.Profile;
import com.gaduationproject.cre8.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final MemberRepository memberRepository;
    private final S3ImageService s3ImageService;
    private final static String MEMBER_PROFILE_IMAGE="member-images/";
    private final static String DEFAULT_PROFILE_URL="";



    public ProfileWithUserInfoResponseDto showMyProfile(final String memberId){

        Member member = getLoginMember(memberId);

        Profile profile = member.getProfile();

        return ProfileWithUserInfoResponseDto.builder().profile(profile)
                .userNickName(member.getNickName())
                        .accessUrl(member.getAccessUrl())
                                .build();

    }

    @Transactional
    public void changeMemberProfile(final String loginId , final ProfileWithUserInfoEditRequestDto profileWithUserInfoEditRequestDto){

        Member member = getLoginMember(loginId);

        Profile profile = member.getProfile();

        profile.changeProfile(profileWithUserInfoEditRequestDto.getYoutubeLink(),
                profileWithUserInfoEditRequestDto.getPersonalLink(), profileWithUserInfoEditRequestDto.getTwitterLink(), profileWithUserInfoEditRequestDto.getPersonalStatement());

        changeUserInfo(profileWithUserInfoEditRequestDto, member);


    }

    private void changeUserInfo(ProfileWithUserInfoEditRequestDto profileWithUserInfoEditRequestDto,
            Member member) {

        System.out.println("안녕"+ profileWithUserInfoEditRequestDto.getMultipartFile());

        if(!checkDefaultProfileUrl(member.getAccessUrl())){
            s3ImageService.deleteImage(member.getAccessUrl());
        }

        String accessUrl = getAccessUrl(profileWithUserInfoEditRequestDto.getMultipartFile(),member);

        member.changeAccessUrlAndNickName(accessUrl,
                profileWithUserInfoEditRequestDto.getUserNickName());
    }

    private boolean checkDefaultProfileUrl(String accessUrl){
        if(accessUrl.equals(DEFAULT_PROFILE_URL)){
            return true;
        }

        return false;
    }

    private String getAccessUrl(MultipartFile multipartFile,Member member){
        if(multipartFile==null || multipartFile.isEmpty()){
            return member.getAccessUrl();
        }

        return s3ImageService.saveImage(multipartFile,MEMBER_PROFILE_IMAGE,
                multipartFile.getOriginalFilename());
    }

    private Member getLoginMember(final String loginId){
        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.LOGIN_ID_NOT_MATCH));
    }





}
