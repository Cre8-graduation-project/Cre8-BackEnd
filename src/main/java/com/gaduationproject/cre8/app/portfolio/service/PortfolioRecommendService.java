package com.gaduationproject.cre8.app.portfolio.service;

import com.gaduationproject.cre8.app.portfolio.dto.request.PortfolioAIRequestDto;
import com.gaduationproject.cre8.app.portfolio.dto.request.PortfolioRecommendRequestDto;
import com.gaduationproject.cre8.app.portfolio.dto.response.PortfolioRecommendResponseDto;
import com.gaduationproject.cre8.app.portfolio.dto.response.PortfolioResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioRecommendService {

    private final WebClient webClient;

    public List<PortfolioRecommendResponseDto> showRecommendPortfolio(final PortfolioRecommendRequestDto portfolioRecommendRequestDto){

        List<PortfolioAIRequestDto> portfolioAIRequestDtoList = webClient.post()
                .uri("/find_similar_image")
                .bodyValue(portfolioRecommendRequestDto)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<PortfolioAIRequestDto>>() {})
                .block();

        return portfolioAIRequestDtoList.stream()
                .map(portfolioAIRequestDto -> PortfolioRecommendResponseDto.builder()
                        .id(portfolioAIRequestDto.getMost_similar_portfolio_id())
                        .accessUrl(portfolioAIRequestDto.getMost_similar_access_url())
                        .similarity(portfolioAIRequestDto.getSimilarity_score())
                        .build())
                .toList();

    }

}
