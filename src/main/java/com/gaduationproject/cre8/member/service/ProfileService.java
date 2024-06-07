package com.gaduationproject.cre8.member.service;

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

    public ProfileResponseDto showProfile(Member member){

        Profile profile = member.getProfile();

        return ProfileResponseDto.builder()
                .profile(profile)
                .build();

    }




}
