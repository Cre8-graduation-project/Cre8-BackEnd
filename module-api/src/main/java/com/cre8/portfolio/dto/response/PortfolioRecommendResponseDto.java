package com.cre8.portfolio.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioRecommendResponseDto {

    private Long id;

    private String accessUrl;

    private String similarity;

    @Builder
    public PortfolioRecommendResponseDto(final Long id,final String accessUrl, final String similarity){

        this.id = id;
        this.accessUrl = accessUrl;
        this.similarity = similarity;

    }

}
