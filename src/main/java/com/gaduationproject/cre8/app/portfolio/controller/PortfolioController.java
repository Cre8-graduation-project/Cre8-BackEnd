package com.gaduationproject.cre8.app.portfolio.controller;

import com.gaduationproject.cre8.app.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.app.response.BaseResponse;
import com.gaduationproject.cre8.app.portfolio.dto.request.PortfolioEditRequestDto;
import com.gaduationproject.cre8.app.portfolio.dto.response.PortfolioResponseDto;
import com.gaduationproject.cre8.app.portfolio.dto.response.PortfolioSimpleResponseDto;
import com.gaduationproject.cre8.app.portfolio.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolios")
@Tag(name = "포트폴리오 관련 컨트롤러", description = "포트폴리오와 관련된 기능을 수행하는 컨트롤러입니다.")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping
    @Operation(summary = "포트폴리오 생성",description = "포트폴리오를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "포트폴리오 성공적으로 생성")
    })
    public ResponseEntity<BaseResponse<Long>> savePortfolio(@CurrentMemberLoginId final String loginId){

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.createSuccess(portfolioService.savePortfolio(loginId)));
    }


    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "포트폴리오 수정",description = "포트폴리오를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "포트폴리오 성공적으로 수정"),
            @ApiResponse(responseCode = "400",description = "포트폴리오의 주인이 아니면 수정할 수 없습니다."),
            @ApiResponse(responseCode = "400",description = "포트폴리오의 하위태그의 부모가 올바른 부모인지 확인하세요"),
            @ApiResponse(responseCode = "404",description = "작업 태그를 찾을 수 없습니다"),
            @ApiResponse(responseCode = "404",description = "하위 태그를 찾을 수 없습니다.")
    })
    public ResponseEntity<Void> changePortfolio(@CurrentMemberLoginId final String loginId,@Valid @ModelAttribute
    final PortfolioEditRequestDto portfolioEditRequestDto){

        portfolioService.updatePortfolio(loginId, portfolioEditRequestDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{portfolioId}")
    @Operation(summary = "포트폴리오 단일 조회",description = "포트폴리오를 ID 기반으로 하나 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "포트폴리오를 찾을 수 없습니다. ID 확인")
    })
    public ResponseEntity<BaseResponse<PortfolioResponseDto>> showPortfolio(@PathVariable("portfolioId") final Long portfolioId){

        return ResponseEntity.ok(BaseResponse.createSuccess(portfolioService.showPortfolio(portfolioId)));
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "포트폴리오를 여러개 이미지기반으로 조회합니다",description = "포트폴리오 처음 이미지와 ID 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "Member 의 ID를 확인할 수 없습니다.")
    })
    public ResponseEntity<BaseResponse<List<PortfolioSimpleResponseDto>>> showSimplePortfolio(@PathVariable("memberId") final Long memberId){

        return ResponseEntity.ok(BaseResponse.createSuccess(portfolioService.showPortfolioList(memberId)));
    }

    @DeleteMapping("/{portfolioId}")
    @Operation(summary = "포트폴리오를 삭제합니다",description = "포트폴리오를 ID 기반으로 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "포트폴리오를 찾을 수 없습니다"),
            @ApiResponse(responseCode = "400",description = "자신의 포트폴리오만 수정할 수 있습니다")
    })
    public ResponseEntity<Void> deletePortfolio(@CurrentMemberLoginId final String loginId, @PathVariable("portfolioId") final Long portfolioId){

        portfolioService.deletePortfolio(loginId,portfolioId);

        return ResponseEntity.ok().build();

    }



}
