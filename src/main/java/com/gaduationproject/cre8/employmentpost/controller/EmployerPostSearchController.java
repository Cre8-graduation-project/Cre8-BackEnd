package com.gaduationproject.cre8.employmentpost.controller;

import com.gaduationproject.cre8.common.response.BaseResponse;
import com.gaduationproject.cre8.employmentpost.dto.request.EmployerPostSearch;
import com.gaduationproject.cre8.employmentpost.dto.response.EmployerPostSearchResponseDto;
import com.gaduationproject.cre8.employmentpost.dto.response.EmployerPostSearchWithCountResponseDto;
import com.gaduationproject.cre8.employmentpost.service.EmployerPostSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employer-post/search")
@Tag(name = "구인자 게시글 검색 컨트롤러")
public class EmployerPostSearchController {

    private final EmployerPostSearchService employerPostSearchService;


    @GetMapping
    @Operation(summary = "구인자 게시글 검색",description = "구인자 게시글을 다양한 조건으로 검색할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "구인자 게시글 성공적 검색")

    })
    public ResponseEntity<BaseResponse<EmployerPostSearchWithCountResponseDto>> employerPostSearchResponse(final EmployerPostSearch employerPostSearch,
            @PageableDefault(size = 10,sort = "createdAt",direction = Direction.DESC,page = 0) final Pageable pageable){

        return ResponseEntity.ok(BaseResponse.createSuccess(employerPostSearchService.searchEmployerPost(employerPostSearch,pageable)));
    }

}
