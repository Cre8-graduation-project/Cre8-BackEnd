package com.gaduationproject.cre8.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gaduationproject.cre8.member.dto.MemberSignUpRequestDto;
import com.gaduationproject.cre8.member.dto.ProfileEditRequestDto;
import com.gaduationproject.cre8.member.dto.ProfileResponseDto;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.entity.Profile;
import com.gaduationproject.cre8.member.repository.MemberRepository;
import com.gaduationproject.cre8.member.type.Sex;
import java.time.LocalDate;
import java.util.Optional;
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

    @Mock
    MemberRepository memberRepository;

    private Member getDefaultMember(){
        Member member = Member.builder()
                .name("이진우")
                .loginId("dionisos198")
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

    private Member getChangedMember(){
        Member member = Member.builder()
                .name("이진우")
                .loginId("dionisos198")
                .email("dionisos198@naver.com")
                .password("password")
                .sex(Sex.M)
                .nickName("dionisos198")
                .birthDay(LocalDate.of(2023,1,1))
                .profile(Profile.builder()
                        .twitterLink("www.jinu.com")
                        .youtubeLink(null)
                        .personalStatement("나는 박살이다")
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
        Member member = getDefaultMember();
        when(memberRepository.findMemberByLoginId(any(String.class))).thenReturn(Optional.of(member));


        //when
        ProfileResponseDto profileResponseDto = profileService.showMyProfile(member.getLoginId());

        //then
        assertThat(profileResponseDto.getYoutubeLink()).isEqualTo(null);
        assertThat(profileResponseDto.getTwitterLink()).isEqualTo(null);
        assertThat(profileResponseDto.getPersonalLink()).isEqualTo(null);
        assertThat(profileResponseDto.getPersonalStatement()).isEqualTo(null);

    }

    @Test
    @DisplayName("유저 프로필 수정")
    public void 프로필_수정(){

        //given
        Member member = getDefaultMember();
        ProfileEditRequestDto profileEditRequestDto = ProfileEditRequestDto.builder()
                                                                            .youtubeLink("www.youtube.com")
                                                                            .twitterLink("www.twitter.com")
                                                                            .personalLink("www.personalLink.com")
                                                                            .personalStatement("저는 진짜 편집의 신입니다")
                                                                            .build();
        when(memberRepository.findMemberByLoginId(any(String.class))).thenReturn(Optional.of(member));


        //when
        profileService.changeMemberProfile(member.getLoginId(), profileEditRequestDto);

        //then
        assertThat(member.getProfile().getYoutubeLink()).isEqualTo("www.youtube.com");
        assertThat(member.getProfile().getTwitterLink()).isEqualTo("www.twitter.com");
        assertThat(member.getProfile().getPersonalLink()).isEqualTo("www.personalLink.com");
        assertThat(member.getProfile().getPersonalStatement()).isEqualTo("저는 진짜 편집의 신입니다");

    }

    @Test
    @DisplayName("유저가 profile 수정한 적이 있을 때 profile 조회")
    public void 유저_수정후_Profile_조회(){

        //given
        Member member = getChangedMember();
        when(memberRepository.findMemberByLoginId(any(String.class))).thenReturn(Optional.of(member));

        //when
        ProfileResponseDto profileResponseDto = profileService.showMyProfile(member.getLoginId());

        //then
        assertThat(profileResponseDto.getYoutubeLink()).isEqualTo(null);
        assertThat(profileResponseDto.getTwitterLink()).isEqualTo("www.jinu.com");
        assertThat(profileResponseDto.getPersonalLink()).isEqualTo(null);
        assertThat(profileResponseDto.getPersonalStatement()).isEqualTo("나는 박살이다");

    }



}