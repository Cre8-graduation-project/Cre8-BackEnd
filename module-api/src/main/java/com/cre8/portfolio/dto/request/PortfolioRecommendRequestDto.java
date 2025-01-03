package com.cre8.portfolio.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioRecommendRequestDto {

    private MultipartFile imageFile;

    private String imageUrl;

    @Builder
    public PortfolioRecommendRequestDto(MultipartFile imageFile, String imageUrl) {
        this.imageFile = imageFile;
        this.imageUrl = imageUrl;
    }
}
