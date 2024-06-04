package com.gaduationproject.cre8.common.config;

import com.gaduationproject.cre8.auth.handler.JwtAccessDeniedHandler;
import com.gaduationproject.cre8.auth.handler.JwtAuthenticationEntryPointHandler;
import com.gaduationproject.cre8.auth.jwt.JwtFilter;
import com.gaduationproject.cre8.auth.jwt.TokenProvider;
import com.gaduationproject.cre8.member.entity.Authority;
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
    private final RedisTemplate<String, String> redisTemplate;


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
                                requestMatchers(HttpMethod.GET,"/post/*").permitAll()
                                .requestMatchers("/test/login").hasAuthority(Authority.NORMAL.toString())
                               // .requestMatchers("/post/*").hasAuthority("GENERAL")
                                .anyRequest().permitAll())

                .addFilterBefore(new JwtFilter(tokenProvider,redisTemplate),
                        UsernamePasswordAuthenticationFilter.class);


        return http.build();

    }
}
