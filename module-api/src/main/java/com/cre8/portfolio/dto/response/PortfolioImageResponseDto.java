package com.cre8.portfolio.dto.response;

import com.cre8.portfolio.entity.PortfolioImage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioImageResponseDto {

    private Long portfolioImageId;
    private String portfolioImageAccessUrl;

    public static PortfolioImageResponseDto of(PortfolioImage portfolioImage){

        return new PortfolioImageResponseDto(portfolioImage.getId(),portfolioImage.getAccessUrl());

    }

}
