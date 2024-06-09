package com.gaduationproject.cre8.member.service;

import com.gaduationproject.cre8.auth.interfaces.CurrentMember;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.member.dto.ProfileEditRequestDto;
import com.gaduationproject.cre8.member.dto.ProfileResponseDto;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.entity.Profile;
import com.gaduationproject.cre8.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final MemberRepository memberRepository;



    public ProfileResponseDto showMyProfile(final String loginId){

        Member member = getLoginMember(loginId);

        Profile profile = member.getProfile();

        return ProfileResponseDto.builder().profile(profile).build();

    }

    @Transactional
    public void changeMemberProfile(final String loginId , final ProfileEditRequestDto profileEditRequestDto){

        Member member = getLoginMember(loginId);

        Profile profile = member.getProfile();

        profile.changeProfile(profileEditRequestDto.getYoutubeLink(),
                profileEditRequestDto.getPersonalLink(), profileEditRequestDto.getTwitterLink(), profileEditRequestDto.getPersonalStatement());
    }

    private Member getLoginMember(final String loginId){
        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.LOGIN_ID_NOT_MATCH));
    }




}
