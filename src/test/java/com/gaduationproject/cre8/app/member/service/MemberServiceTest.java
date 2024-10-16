package com.gaduationproject.cre8.app.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.gaduationproject.cre8.app.member.dto.MemberSignUpRequestDto;
import com.gaduationproject.cre8.common.response.error.exception.DuplicateException;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.domain.member.type.Sex;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private Member getMember(){
        Member member = Member.builder()
                .name("이진우")
                .email("dionisos198@naver.com")
                .password("password")
                .sex(Sex.M)
                .nickName("dionisos198")
                .birthDay(LocalDate.of(2023,1,1))
                .build();

        ReflectionTestUtils.setField(member,"id",1L);

        return member;
    }

    @Test
    @DisplayName("회원 저장 로직- 정상 상황")
    public void 회원_저장(){

        //givne
        Member member = getMember();


        //when
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn("sdfksdflskjdflsdj");
        MemberSignUpRequestDto memberSignUpRequestDto = MemberSignUpRequestDto
                .builder()
                .name("이진우")
                .email("dionisos198@naver.com")
                .password("password")
                .sex(Sex.M)
                .nickName("dionisos198")
                .birthDay(LocalDate.of(2023,1,1))
                .build();


        //then
        Long findMemberId = memberService.saveMember(memberSignUpRequestDto);

        //then
        assertThat(findMemberId).isEqualTo(1L);
    }

    @Test
    @DisplayName("회원 저장 로직- 이메일이 중복되었을 때 ")
    public void 회원_저장_이메일_중복_예외(){

        //given
        Member member = getMember();

        //when
        when(memberRepository.existsByEmail(eq(member.getEmail()))).thenReturn(Boolean.TRUE);
        MemberSignUpRequestDto memberSignUpRequestDto = MemberSignUpRequestDto
                .builder()
                .name("이진우1")
                .email("dionisos198@naver.com")
                .password("password1")
                .sex(Sex.M)
                .nickName("dionisos1981")
                .birthDay(LocalDate.of(2023,1,2))
                .build();


        //then

        Exception e = assertThrows(DuplicateException.class,()->{
            memberService.saveMember(memberSignUpRequestDto);
        });

        assertEquals("이메일이 중복되었습니다", e.getMessage());

    }

    @Test
    @DisplayName("회원 저장 로직- 닉네임이 중복되었을 때 ")
    public void 회원_저장_닉네임_중복_예외(){
        //given
        Member member = getMember();

        //when
        when(memberRepository.existsByNickName(eq(member.getNickName()))).thenReturn(Boolean.TRUE);
        MemberSignUpRequestDto memberSignUpRequestDto = MemberSignUpRequestDto
                .builder()
                .name("이진우1")
                .email("dionisos1981@naver.com")
                .password("password1")
                .sex(Sex.M)
                .nickName("dionisos198")
                .birthDay(LocalDate.of(2023,1,2))
                .build();

        //then
        Exception e = assertThrows(DuplicateException.class,()->{
            memberService.saveMember(memberSignUpRequestDto);
        });

        assertEquals("닉네임이 중복되었습니다", e.getMessage());

    }

    @Test
    @DisplayName("회원 저장 로직- 아이디가 중복되었을 때 ")
    public void 회원_저장_아이디_중복_예외(){
        //given
        Member member = getMember();

        //when
        when(memberRepository.existsByLoginId(eq(member.getLoginId()))).thenReturn(Boolean.TRUE);
        MemberSignUpRequestDto memberSignUpRequestDto = MemberSignUpRequestDto
                .builder()
                .name("이진우1")
                .email("dionisos1981@naver.com")
                .password("password1")
                .sex(Sex.M)
                .nickName("dionisos198")
                .birthDay(LocalDate.of(2023,1,2))
                .build();

        //then
        Exception e = assertThrows(DuplicateException.class,()->{
            memberService.saveMember(memberSignUpRequestDto);
        });

        assertEquals("이미 아이디가 존재합니다", e.getMessage());

    }





}