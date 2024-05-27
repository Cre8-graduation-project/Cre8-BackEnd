package com.gaduationproject.cre8.member.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.DuplicateException;
import com.gaduationproject.cre8.member.dto.MemberSignUpRequestDto;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberSignUpService {
    private final MemberRepository memberRepository;


    @Transactional
    public Long saveMember(final MemberSignUpRequestDto memberSignUpRequestDto){

        if(memberRepository.existsByEmail(memberSignUpRequestDto.getEmail())){
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }

        if(memberRepository.existsByNickName(memberSignUpRequestDto.getNickName())){
            throw new DuplicateException(ErrorCode.DUPLICATE_NICKNAME);
        }

        Member member = Member.builder()
                .email(memberSignUpRequestDto.getEmail())
                .name(memberSignUpRequestDto.getName())
                .sex(memberSignUpRequestDto.getSex())
                .password(memberSignUpRequestDto.getPassword())
                .nickName(memberSignUpRequestDto.getNickName())
                .birthDay(memberSignUpRequestDto.getBirthDay())
                .build();


        return memberRepository.save(member).getId();


    }

}
