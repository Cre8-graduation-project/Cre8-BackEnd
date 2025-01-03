package com.app.common;


import com.cre8.auth.service.CustomUserDetails;
import com.cre8.member.entity.Authority;
import com.cre8.member.entity.Member;
import com.cre8.member.type.Sex;
import java.time.LocalDate;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser user) {

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(Authority.NORMAL.toString());

        Member member = Member.builder()
                .email("dionisos198@gmail.com")
                .sex(Sex.M)
                .nickName("hihi")
                .name(user.name())
                .password("password")
                .loginId(user.loginId())
                .birthDay(LocalDate.now())
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(member.getLoginId(),"password",true,true,true,true,
                Collections.singleton(simpleGrantedAuthority),member);

        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails,"",customUserDetails.getAuthorities());

        securityContext.setAuthentication(authentication);

        return securityContext;
    }
}
