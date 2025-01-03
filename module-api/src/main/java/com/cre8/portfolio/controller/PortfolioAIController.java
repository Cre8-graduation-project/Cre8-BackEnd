package com.cre8.portfolio.controller;


import com.cre8.portfolio.dto.request.PortfolioRecommendRequestDto;
import com.cre8.portfolio.dto.response.PortfolioRecommendResponseDto;
import com.cre8.portfolio.service.PortfolioRecommendService;
import com.cre8.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolios/ai")
@Tag(name = "포트폴리오 관련 컨트롤러", description = "포트폴리오와 관련된 기능을 수행하는 컨트롤러입니다.")
public class PortfolioAIController {

    private final PortfolioRecommendService portfolioRecommendService;

    @PostMapping(value = "/recommend",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "포트폴리오 추천",description = "제공된 원격 url 을 바탕으로 포트폴리오 추천")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "성공적 추천")
    })
    public ResponseEntity<BaseResponse<List<PortfolioRecommendResponseDto>>> showRecommendPortfolio(@Valid @ModelAttribute final
    PortfolioRecommendRequestDto portfolioRecommendRequestDto){

        return ResponseEntity.ok(BaseResponse.createSuccess(portfolioRecommendService.showRecommendPortfolio(portfolioRecommendRequestDto)));
    }

}
