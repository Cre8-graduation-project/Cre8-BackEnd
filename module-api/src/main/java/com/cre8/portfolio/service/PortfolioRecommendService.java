package com.cre8.portfolio.service;


import com.cre8.portfolio.dto.request.PortfolioAIRequestDto;
import com.cre8.portfolio.dto.request.PortfolioRecommendRequestDto;
import com.cre8.portfolio.dto.response.PortfolioRecommendResponseDto;
import com.cre8.response.error.ErrorCode;
import com.cre8.response.error.exception.BadRequestException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.BodyInserters.MultipartInserter;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioRecommendService {

    private final WebClient webClient;

    private static final String QUERY_IMAGE_URL = "query_image_url";
    private static final String QUERY_IMAGE_FILE = "query_image_file";

    private static final String ML_RECOMMEND_API="/find_similar_image";

    private static final String SAVE_VECTOR = "/portfolio/vector";

    public List<PortfolioRecommendResponseDto> showRecommendPortfolio(final PortfolioRecommendRequestDto portfolioRecommendRequestDto){


        List<PortfolioAIRequestDto> portfolioAIRequestDtoList = webClient.post()
                .uri(ML_RECOMMEND_API)
                .body(getBody(portfolioRecommendRequestDto))
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

    public void savePortfolioWithVector(final Long portfolioImageId){

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(SAVE_VECTOR)
                        .queryParam("portfolioImageId", portfolioImageId)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private MultipartInserter getBody(final PortfolioRecommendRequestDto portfolioRecommendRequestDto) {

        if (!multiPartFileBlank(portfolioRecommendRequestDto.getImageFile())) {

            return BodyInserters.fromMultipartData(QUERY_IMAGE_FILE, portfolioRecommendRequestDto.getImageFile().getResource());
        }

        if(imageUrlBlank(portfolioRecommendRequestDto.getImageUrl())){
            throw new BadRequestException(ErrorCode.CANT_ALL_BLANK_FILE_URL);
        }

        return BodyInserters.fromMultipartData(QUERY_IMAGE_URL, portfolioRecommendRequestDto.getImageUrl());
    }

    private boolean multiPartFileBlank(final MultipartFile multipartFile){
        return multipartFile==null || multipartFile.isEmpty();
    }

    private boolean imageUrlBlank(final String imageUrl){
        return imageUrl==null || imageUrl.isBlank();
    }

}
