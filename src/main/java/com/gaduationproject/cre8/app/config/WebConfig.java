package com.gaduationproject.cre8.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // cors를 적용할 spring서버의 url 패턴.
                .allowedOrigins(
                        "http://localhost:3000","http://43.200.165.75:3000"
                ,"http://testcre8.co.kr","https://testcre8.co.kr","https://localhost:3000",
                "https://test.testcre8.co.kr:3000") // cors를 허용할 도메인. 제한을 모두 해제하려면 "**"
                .allowedMethods("GET", "POST", "PUT", "PATCH",
                        "DELETE", "OPTIONS") // cors를 허용할 method + DELETE 추가
                .allowedHeaders("Content-Type", "Authorization","accessToken","refreshToken")
                .exposedHeaders("Authorization","Set-Cookie")
                .allowCredentials(true);

    }
}

