package com.gaduationproject.cre8.app.portfolio.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioAIRequestDto {

    private String most_similar_access_url;

    private Long most_similar_portfolio_id;

    private String similarity_score;



}
