package com.gaduationproject.cre8.app.auth.service;

import com.gaduationproject.cre8.app.auth.dto.SignInRequestDto;
import com.gaduationproject.cre8.app.auth.dto.TokenDto;
import com.gaduationproject.cre8.app.auth.dto.TokenReIssueResponseDto;
import com.gaduationproject.cre8.app.auth.dto.TokenResponseWithUserBasicInfoDto;
import com.gaduationproject.cre8.app.auth.jwt.TokenProvider;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.externalApi.redis.service.RedisUtil;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisUtil redisUtil;





    @Transactional
    public TokenResponseWithUserBasicInfoDto login(final SignInRequestDto signInRequestDto){


        Member findMember=memberRepository.findMemberByLoginId(signInRequestDto.getUserID()).orElseThrow(()->new BadRequestException(
                ErrorCode.LOGIN_ID_NOT_MATCH));

        if(!passwordEncoder.matches(signInRequestDto.getPassword(),findMember.getPassword())){
            throw new BadRequestException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        UsernamePasswordAuthenticationToken authenticationToken= signInRequestDto.getAuthenticationToken();
        Authentication authentication= authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto=tokenProvider.createToken(authentication);

        redisUtil.saveAtRedis(authentication.getName(),tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(),TimeUnit.MILLISECONDS);

        return new TokenResponseWithUserBasicInfoDto(tokenDto.getType(),tokenDto.getAccessToken(),
                makeResponseCookie(tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime()),
                tokenDto.getAccessTokenValidationTime(),findMember.getLoginId(),findMember.getId(),findMember.getAccessUrl());

    }


    @Transactional
    public TokenReIssueResponseDto reIssue(final String accessToken, final String refreshToken){

        Authentication authentication= tokenProvider.getAuthentication(accessToken);

        if(!redisUtil.getValueByKey(authentication.getName()).equals(refreshToken)){
            throw new BadRequestException(ErrorCode.REFRESH_TOKEN_NOT_MATCH);
        }

        TokenDto tokenDto=tokenProvider.createToken(authentication);
        redisUtil.saveAtRedis(authentication.getName(),tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(),TimeUnit.MILLISECONDS);

        return new TokenReIssueResponseDto(tokenDto.getType(),tokenDto.getAccessToken(),
                makeResponseCookie(tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime()),tokenDto.getAccessTokenValidationTime());
    }

    @Transactional
    public void logout(final String accessToken){

        if (!tokenProvider.validateToken(accessToken)){
            throw new BadRequestException(ErrorCode.ACCESS_TOKEN_NOT_MATCH);
        }

        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        if (redisUtil.getValueByKey(authentication.getName())!=null){
            redisUtil.deleteAtRedis(authentication.getName());
        }


        Long expiration = tokenProvider.getExpiration(accessToken);
        redisUtil.saveAtRedis(accessToken,"logout",expiration,TimeUnit.MILLISECONDS);

    }

    private ResponseCookie makeResponseCookie(String refreshToken,Long refreshTokenValidationTime){
        return  ResponseCookie.from("refreshToken",refreshToken)
                .httpOnly(true)//   true 시 자바스크립트에서 쿠키 접근 불가 따라서 XSS 공격 방지
                .secure(true)//true 시 HTTPS 연결을 통해서만 전달 .
                .path("/")
                .maxAge(refreshTokenValidationTime)
                .sameSite("None")
                .build();
    }


}

