package com.gaduationproject.cre8.app.employmentpost.controller;

import com.gaduationproject.cre8.app.response.BaseResponse;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployerPostSearch;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostSearchWithCountResponseDto;
import com.gaduationproject.cre8.app.employmentpost.service.EmployerPostSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employer-posts/search")
@Tag(name = "구인자 게시글 검색 컨트롤러")
public class EmployerPostSearchController {

    private final EmployerPostSearchService employerPostSearchService;


    @GetMapping
    @Operation(summary = "구인자 게시글 검색",description = "구인자 게시글을 다양한 조건으로 검색할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "구인자 게시글 성공적 검색")

    })
    @Parameters({
            @Parameter(name = "workFieldId", description = "상위 분야 태그 아이디", in = ParameterIn.QUERY),
            @Parameter(name = "workFieldChildTagId", description = "하위 분야 태그 리스트", in = ParameterIn.QUERY),
            @Parameter(name = "minCareer", description = "최소 경력 검색 조건", in = ParameterIn.QUERY),
            @Parameter(name = "maxCareer", description = "최대 경력 검색 조건", in = ParameterIn.QUERY),
            @Parameter(name = "page", description = "페이지 번호(0부터 시작)", in = ParameterIn.QUERY),
            @Parameter(name = "direction", description = "내림차순과 오름차순(desc,asc)", in = ParameterIn.QUERY),
            @Parameter(name = "sort", description = "정렬기준(createdAt, deadLine,hopeCareer)", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "페이지당 아이템 갯수", in = ParameterIn.QUERY)
    }
    )
    public ResponseEntity<BaseResponse<EmployerPostSearchWithCountResponseDto>> employerPostSearchResponse(final EmployerPostSearch employerPostSearch,
            @PageableDefault(size = 10,sort = "createdAt",direction = Direction.DESC,page = 0) final Pageable pageable){

        return ResponseEntity.ok(BaseResponse.createSuccess(employerPostSearchService.searchEmployerPost(employerPostSearch,pageable)));
    }

}
