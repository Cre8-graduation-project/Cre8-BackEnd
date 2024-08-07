package com.gaduationproject.cre8.api.portfolio.dto.response;

import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioSimpleResponseDto {

    private Long id;
    private String accessUrl;

    public static PortfolioSimpleResponseDto from(Portfolio portfolio){
        return new PortfolioSimpleResponseDto(portfolio.getId(),
                portfolio.getPortfolioImageList().isEmpty()?null:portfolio.getPortfolioImageList().get(0).getAccessUrl());
    }


}
