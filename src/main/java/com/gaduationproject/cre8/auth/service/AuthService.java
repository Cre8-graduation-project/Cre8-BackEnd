package com.gaduationproject.cre8.auth.service;

import com.gaduationproject.cre8.auth.dto.SignInRequestDto;
import com.gaduationproject.cre8.auth.dto.TokenDto;
import com.gaduationproject.cre8.auth.dto.TokenReIssueResponseDto;
import com.gaduationproject.cre8.auth.dto.TokenResponseDto;
import com.gaduationproject.cre8.auth.jwt.TokenProvider;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.repository.MemberRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate<String,String> redisTemplate;





    @Transactional
    public TokenResponseDto login(final SignInRequestDto signInRequestDto){

        System.out.println(signInRequestDto.getUserID());

        Member findMember=memberRepository.findMemberByLoginId(signInRequestDto.getUserID()).orElseThrow(()->new BadRequestException(
                ErrorCode.LOGIN_ID_NOT_MATCH));

        if(!passwordEncoder.matches(signInRequestDto.getPassword(),findMember.getPassword())){
            throw new BadRequestException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        UsernamePasswordAuthenticationToken authenticationToken= signInRequestDto.getAuthenticationToken();
        Authentication authentication= authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto=tokenProvider.createToken(authentication);

        saveLoginProcessAtRedis(authentication.getName(),tokenDto);

        return new TokenResponseDto(tokenDto.getType(),tokenDto.getAccessToken(),
                tokenDto.getRefreshToken(),tokenDto.getAccessTokenValidationTime());

    }


    @Transactional
    public TokenReIssueResponseDto reIssue(final String accessToken, final String refreshToken){

        Authentication authentication= tokenProvider.getAuthentication(accessToken);

        if(!redisTemplate.opsForValue().get(authentication.getName()).equals(refreshToken)){
            throw new BadRequestException(ErrorCode.REFRESH_TOKEN_NOT_MATCH);
        }

        TokenDto tokenDto=tokenProvider.createToken(authentication);
        saveLoginProcessAtRedis(authentication.getName(),tokenDto);

        return new TokenReIssueResponseDto(tokenDto.getType(),tokenDto.getAccessToken(),
                makeResponseCookie(tokenDto.getRefreshToken()),tokenDto.getAccessTokenValidationTime());
    }

    @Transactional
    public void logout(final String accessToken){

        if (!tokenProvider.validateToken(accessToken)){
            throw new BadRequestException(ErrorCode.ACCESS_TOKEN_NOT_MATCH);
        }

        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        if (redisTemplate.opsForValue().get(authentication.getName())!=null){
            redisTemplate.delete(authentication.getName());
        }


        Long expiration = tokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken,"logout",expiration,TimeUnit.MILLISECONDS);


    }

    private ResponseCookie makeResponseCookie(String refreshToken){
        return  ResponseCookie.from("refreshToken",refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60)
                .build();
    }

    private void saveLoginProcessAtRedis(String key, TokenDto tokenDto){
        redisTemplate.opsForValue().set(key,tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(),TimeUnit.MILLISECONDS);
    }

}

