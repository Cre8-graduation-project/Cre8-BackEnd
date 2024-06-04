package com.gaduationproject.cre8.auth.jwt;

import com.gaduationproject.cre8.auth.service.CustomUserDetails;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.member.entity.Authority;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.repository.MemberRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;



    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findMemberByLoginId(username)
                .map(this::getUserDetails)
                .orElseThrow(()->new BadRequestException(ErrorCode.LOGIN_ID_NOT_MATCH));
    }

    public CustomUserDetails getUserDetails(Member member) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new CustomUserDetails(member.getLoginId(), member.getPassword(), Collections.singleton(authority),member);
    }
}