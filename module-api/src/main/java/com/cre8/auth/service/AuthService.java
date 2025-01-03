package com.cre8.auth.service;


import com.cre8.auth.dto.SignInRequestDto;
import com.cre8.auth.dto.TokenDto;
import com.cre8.auth.dto.TokenReIssueResponseDto;
import com.cre8.auth.dto.TokenResponseWithUserBasicInfoDto;
import com.cre8.auth.jwt.TokenProvider;
import com.cre8.member.entity.Member;
import com.cre8.member.repository.MemberRepository;
import com.cre8.redis.service.RedisUtil;
import com.cre8.response.error.ErrorCode;
import com.cre8.response.error.exception.BadRequestException;
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
                tokenDto.getAccessTokenValidationTime(),findMember.getLoginId(),findMember.getId(),findMember.getAccessUrl(),
                findMember.isTmpPassword());

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

