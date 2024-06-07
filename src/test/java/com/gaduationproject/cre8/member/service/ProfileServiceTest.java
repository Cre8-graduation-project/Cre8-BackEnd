package com.gaduationproject.cre8.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gaduationproject.cre8.member.dto.MemberSignUpRequestDto;
import com.gaduationproject.cre8.member.dto.ProfileResponseDto;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.entity.Profile;
import com.gaduationproject.cre8.member.type.Sex;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @InjectMocks
    ProfileService profileService;

    private Member getMember(){
        Member member = Member.builder()
                .name("이진우")
                .email("dionisos198@naver.com")
                .password("password")
                .sex(Sex.M)
                .nickName("dionisos198")
                .birthDay(LocalDate.of(2023,1,1))
                .profile(Profile.builder()
                        .twitterLink(null)
                        .youtubeLink(null)
                        .personalStatement(null)
                        .personalLink(null)
                        .build())
                .build();

        ReflectionTestUtils.setField(member,"id",1L);

        return member;
    }

    @Test
    @DisplayName("유저가 처음 생성되었을떄 profile 조회")
    public void 유저_빈_Profile_조회(){

        //given
        Member member = getMember();


        //when
        ProfileResponseDto profileResponseDto = profileService.showProfile(member);

        //then
        assertThat(profileResponseDto.getYoutubeLink()).isEqualTo(null);
        assertThat(profileResponseDto.getTwitterLink()).isEqualTo(null);
        assertThat(profileResponseDto.getPersonalLink()).isEqualTo(null);
        assertThat(profileResponseDto.getPersonalStatement()).isEqualTo(null);

    }



}