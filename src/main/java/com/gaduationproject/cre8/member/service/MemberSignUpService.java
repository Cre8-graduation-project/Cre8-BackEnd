package com.gaduationproject.cre8.member.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.DuplicateException;
import com.gaduationproject.cre8.member.dto.LoginIdCheckResponseDto;
import com.gaduationproject.cre8.member.dto.MemberSignUpRequestDto;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.entity.Profile;
import com.gaduationproject.cre8.member.repository.MemberRepository;
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
                .profile(makeDefaultProfile())
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

    private Profile makeDefaultProfile(){
        return Profile.builder()
                .personalLink(null)
                .twitterLink(null)
                .youtubeLink(null)
                .personalStatement(null)
                .build();
    }
}
