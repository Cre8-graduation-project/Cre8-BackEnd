package com.gaduationproject.cre8.app.auth.config;

import com.gaduationproject.cre8.app.auth.handler.JwtAccessDeniedHandler;
import com.gaduationproject.cre8.app.auth.handler.JwtAuthenticationEntryPointHandler;
import com.gaduationproject.cre8.app.auth.jwt.JwtFilter;
import com.gaduationproject.cre8.app.auth.jwt.TokenProvider;
import com.gaduationproject.cre8.domain.member.entity.Authority;
import com.gaduationproject.cre8.externalApi.redis.service.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationEntryPointHandler authenticationEntryPointHandler;
    private final JwtAccessDeniedHandler accessDeniedHandler;
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exception)-> exception.authenticationEntryPoint(authenticationEntryPointHandler))
                .exceptionHandling((exception)-> exception.accessDeniedHandler(accessDeniedHandler))

                .authorizeHttpRequests((requests) ->
                        requests.
                                requestMatchers(HttpMethod.GET,"/api/v1/test","/api/v1/redis/test","/api/v1/employee/posts/*"
                                ,"/api/v1/employee-posts/search","/api/v1/employer/posts/*","/api/v1/employer-posts/search","/api/v1/members/check",
                                        "/api/v1/*/profile","/api/v1/members/pk","/api/v1/portfolios/*","/api/v1/portfolios/member/*","/api/v1/tags",
                                        "/api/v1/tags/subcategory/*","/api/v1/tags/child/*").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/test","/api/v1/redis/test","/api/v1/auth/login","/api/v1/mail",
                                        "/api/v1/mail/check","/api/v1/members").permitAll()
                                .anyRequest().authenticated())

                .addFilterBefore(new JwtFilter(tokenProvider,redisUtil),
                        UsernamePasswordAuthenticationFilter.class);


        return http.build();

    }
}
