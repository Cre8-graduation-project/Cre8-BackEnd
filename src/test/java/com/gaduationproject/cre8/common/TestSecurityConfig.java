package com.gaduationproject.cre8.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class TestSecurityConfig {

//    private final JwtAuthenticationEntryPointHandler authenticationEntryPointHandler;
//    private final JwtAccessDeniedHandler accessDeniedHandler;



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
                .exceptionHandling((exception)-> exception.authenticationEntryPoint(new TestJwtAuthenticationEntryPointHandler()))
                .exceptionHandling((exception)-> exception.accessDeniedHandler(new TestJwtAccessDeniedHandler()))

                .authorizeHttpRequests((requests) ->
                        requests.
                                requestMatchers(HttpMethod.GET,"/post/*","/api/v1/portfolios/*").permitAll()
                                .requestMatchers("/api/v1/auth/login","/api/v1/mail/*","/api/v1/members/*","/api/v1/*/profile",
                                        "/api/v1/members/pk").permitAll()
                                .requestMatchers("/api/v1/auth/logout","/api/v1/auth/reissue","/api/v1/profiles","/test/login",
                                        "/api/v1/chats/*","/api/v1/portfolios").authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/v1/tags/*","/api/v1/tags").authenticated()
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/tags/*","/api/v1/portfolios/*").authenticated()
                                // .requestMatchers("/post/*").hasAuthority("GENERAL")
                                .anyRequest().permitAll());

        return http.build();




    }

}
