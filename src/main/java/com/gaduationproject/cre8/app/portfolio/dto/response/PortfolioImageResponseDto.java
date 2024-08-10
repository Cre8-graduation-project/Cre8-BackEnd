package com.gaduationproject.cre8.app.portfolio.dto.response;

import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioImage;
import java.util.List;
import java.util.stream.Collectors;
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
