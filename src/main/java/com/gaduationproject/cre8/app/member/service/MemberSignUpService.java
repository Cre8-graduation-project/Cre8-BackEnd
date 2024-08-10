package com.gaduationproject.cre8.app.member.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.DuplicateException;
import com.gaduationproject.cre8.app.member.dto.LoginIdCheckResponseDto;
import com.gaduationproject.cre8.app.member.dto.MemberSignUpRequestDto;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberSignUpService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;



    @Transactional
    public Long saveMember(final MemberSignUpRequestDto memberSignUpRequestDto){

        checkDuplicateLoginId(memberSignUpRequestDto.getLoginId());

        if(memberRepository.existsByNickName(memberSignUpRequestDto.getNickName())){
            throw new DuplicateException(ErrorCode.DUPLICATE_NICKNAME);
        }

        checkDuplicateEmail(memberSignUpRequestDto.getEmail());



        Member member = Member.builder()
                .email(memberSignUpRequestDto.getEmail())
                .loginId(memberSignUpRequestDto.getLoginId())
                .name(memberSignUpRequestDto.getName())
                .sex(memberSignUpRequestDto.getSex())
                .password(passwordEncoder.encode(memberSignUpRequestDto.getPassword()))
                .nickName(memberSignUpRequestDto.getNickName())
                .birthDay(memberSignUpRequestDto.getBirthDay())
                .build();


        return memberRepository.save(member).getId();


    }

    private void checkDuplicateEmail(final String email){
        if(memberRepository.existsByEmail(email)){
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    private void checkDuplicateLoginId(final String loginId){
        if(memberRepository.existsByLoginId(loginId)){
            throw new DuplicateException(ErrorCode.DUPLICATE_LOGIN_ID);
        }
    }

    public LoginIdCheckResponseDto checkExistsLoginId(final String loginId) {

        checkDuplicateLoginId(loginId);

        return LoginIdCheckResponseDto.builder().loginIdChecked(true).build();
    }


}
