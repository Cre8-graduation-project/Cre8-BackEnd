package com.gaduationproject.cre8.app.employmentpost.controller;

import com.gaduationproject.cre8.app.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchWithSliceResponseDto;
import com.gaduationproject.cre8.app.response.BaseResponse;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployeePostSearch;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchWithCountResponseDto;
import com.gaduationproject.cre8.app.employmentpost.service.EmployeePostSearchService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee-posts/search")
@Tag(name = "구인자 게시글 검색 컨트롤러")
public class EmployeePostSearchController {

    private final EmployeePostSearchService employeePostSearchService;


    @GetMapping
    @Operation(summary = "구직자 게시글 검색",description = "구직자 게시글을 다양한 조건으로 검색할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "구직자 게시글 성공적 검색")
    })
    @Parameters({
            @Parameter(name = "workFieldId", description = "상위 분야 태그 아이디", in = ParameterIn.QUERY),
            @Parameter(name = "workFieldChildTagId", description = "하위 분야 태그 리스트", in = ParameterIn.QUERY),
            @Parameter(name = "minCareer", description = "최소 경력 검색 조건", in = ParameterIn.QUERY),
            @Parameter(name = "maxCareer", description = "최대 경력 검색 조건", in = ParameterIn.QUERY),
            @Parameter(name = "page", description = "페이지 번호(0부터 시작)", in = ParameterIn.QUERY),
            @Parameter(name = "direction", description = "내림차순과 오름차순(desc,asc)", in = ParameterIn.QUERY),
            @Parameter(name = "sort", description = "정렬기준(createdAt, careerYear)", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "페이지당 아이템 갯수", in = ParameterIn.QUERY)
    }
    )
    public ResponseEntity<BaseResponse<EmployeePostSearchWithCountResponseDto>> employeePostSearchResponse(final EmployeePostSearch employeePostSearch,
            @PageableDefault(size = 10,sort = "createdAt",direction = Direction.DESC,page = 0) final Pageable pageable){

        return ResponseEntity.ok(BaseResponse.createSuccess(employeePostSearchService.searchEmployeePost(employeePostSearch,pageable)));
    }

    @GetMapping("/keyword")
    @Operation(summary = "구직자 게시글 keyword 기반 검색", description = "구직자 게시글을 keyword 기반으로 검색")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "구직자 게시글 성공적 검색")
    })
    @Parameters({
            @Parameter(name = "keyword",description = "키워드",in = ParameterIn.QUERY),
            @Parameter(name = "page", description = "페이지 번호(0부터 시작)", in = ParameterIn.QUERY),
            @Parameter(name = "direction", description = "내림차순과 오름차순(desc,asc)", in = ParameterIn.QUERY),
            @Parameter(name = "sort", description = "정렬기준(createdAt,careerYear)", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "페이지당 아이템 갯수", in = ParameterIn.QUERY)
    })
    public ResponseEntity<BaseResponse<EmployeePostSearchWithSliceResponseDto>> employeePostSearchByKeywordResponse(
            @RequestParam(value = "keyword",required = false) final String keyword,@PageableDefault(size = 10,sort = "createdAt",direction = Direction.DESC,page = 0) final Pageable pageable){

        return ResponseEntity.ok(BaseResponse.createSuccess(employeePostSearchService.searchEmployeeByKeyword(keyword,pageable)));
    }


    @GetMapping("/my-posts")
    @Operation(summary = "내가 작성한 구직글 조회",description = "내가 작성한 구직글을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "구직자 게시글 성공적 단건 조회"),
    })
    @Parameters({
            @Parameter(name = "keyword",description = "키워드",in = ParameterIn.QUERY),
            @Parameter(name = "page", description = "페이지 번호(0부터 시작)", in = ParameterIn.QUERY),
            @Parameter(name = "direction", description = "내림차순과 오름차순(desc,asc)", in = ParameterIn.QUERY),
            @Parameter(name = "sort", description = "정렬기준(createdAt,careerYear)", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "페이지당 아이템 갯수", in = ParameterIn.QUERY)
    })
    public ResponseEntity<BaseResponse<EmployeePostSearchWithSliceResponseDto>> showMyEmployeePost
            (@CurrentMemberLoginId final String loginId,
             @PageableDefault(size = 10,sort = "createdAt",direction = Direction.DESC,page = 0) final Pageable pageable
            ){


        return ResponseEntity.ok(BaseResponse.createSuccess(employeePostSearchService.searchMyEmployeePost(loginId,pageable)));
    }

    @GetMapping("/my-bookmark")
    @Operation(summary = "내가 즐겨찾기한 구직글 조회",description = "내가 즐겨찾기한 구직글을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "내가 즐겨찾기한 구직자 게시글 성공적 단건 조회"),
    })
    @Parameters({
            @Parameter(name = "keyword",description = "키워드",in = ParameterIn.QUERY),
            @Parameter(name = "page", description = "페이지 번호(0부터 시작)", in = ParameterIn.QUERY),
            @Parameter(name = "direction", description = "내림차순과 오름차순(desc,asc)", in = ParameterIn.QUERY),
            @Parameter(name = "sort", description = "정렬기준(createdAt,careerYear)", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "페이지당 아이템 갯수", in = ParameterIn.QUERY)
    })
    public ResponseEntity<BaseResponse<EmployeePostSearchWithSliceResponseDto>> showMyBookMarkEmployeePost
            (@CurrentMemberLoginId final String loginId,
             @PageableDefault(size = 10,sort = "createdAt",direction = Direction.DESC,page = 0) final Pageable pageable
            ){


        return ResponseEntity.ok(BaseResponse.createSuccess(employeePostSearchService.searchMyBookMarkEmployeePost(loginId,pageable)));
    }

}
