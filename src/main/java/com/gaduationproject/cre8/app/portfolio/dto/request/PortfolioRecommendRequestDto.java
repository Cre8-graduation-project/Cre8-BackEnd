package com.gaduationproject.cre8.app.portfolio.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioRecommendRequestDto {

    private String query_image_url;

}
