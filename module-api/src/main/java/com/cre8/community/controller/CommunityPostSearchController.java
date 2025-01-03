package com.cre8.community.controller;

import com.cre8.auth.interfaces.CurrentMemberLoginId;
import com.cre8.community.dto.response.CommunityPostSearchWithSliceResponseDto;
import com.cre8.community.service.CommunityPostSearchService;
import com.cre8.response.BaseResponse;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community/posts/search")
@Tag(name = "커뮤니티 게시글 검색 관련  컨트롤러 ", description = "커뮤니티 게시글을 리스트를 조회한다. ")
public class CommunityPostSearchController {

    private final CommunityPostSearchService communityPostSearchService;

    @GetMapping("/{communityBoardId}")
    @Operation(summary = "커뮤니티 게시글 리스트",description = "커뮤니티 게시글을 커뮤니티 게시판  최신순으로 조회할 수 있습니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "커뮤니티 게시글 리스트 조회 성공2")
    })
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호(0부터 시작)", in = ParameterIn.QUERY),
            @Parameter(name = "direction", description = "내림차순과 오름차순(desc,asc)", in = ParameterIn.QUERY),
            @Parameter(name = "sort", description = "정렬기준(createdAt)", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "페이지당 아이템 갯수", in = ParameterIn.QUERY)
    }
    )
    public ResponseEntity<BaseResponse<CommunityPostSearchWithSliceResponseDto>> communityPostSearchResponse(
            @PathVariable("communityBoardId") final Long communityBoardId,
            @RequestParam(value = "lastPostId",required = false) final Long lastPostId,
            @PageableDefault(size = 10,sort = "createdAt",direction = Direction.DESC,page = 0) final Pageable pageable) {

        return ResponseEntity.ok(BaseResponse
                .createSuccess(communityPostSearchService.searchCommunityPostByCommunityBoardIdAndLastPostId(communityBoardId,lastPostId,pageable)));
    }



    @GetMapping("/like/my-Post")
    @Operation(summary = "내가 좋아요한 커뮤니티 게시글  조회",description = "내가 좋아요한 커뮤니티 게시글을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "내가 좋아요한 커뮤니티 게시글 성공적 조회"),
    })
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호(0부터 시작)", in = ParameterIn.QUERY),
            @Parameter(name = "direction", description = "내림차순과 오름차순(desc,asc)", in = ParameterIn.QUERY),
            @Parameter(name = "sort", description = "정렬기준(createdAt)", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "페이지당 아이템 갯수", in = ParameterIn.QUERY)
    })
    public ResponseEntity<BaseResponse<CommunityPostSearchWithSliceResponseDto>> showMyLikeCommunityPost
            (@CurrentMemberLoginId final String loginId,
                    @PageableDefault(size = 10,sort = "createdAt",direction = Direction.DESC,page = 0) final Pageable pageable
            ){


        return ResponseEntity.ok(BaseResponse.createSuccess(communityPostSearchService.searchMyLikeCommunityPost(loginId,pageable)));
    }

    @GetMapping("/my-Post")
    @Operation(summary = "내가 작성한 커뮤니티 게시글  조회",description = "내가 작성한 커뮤니티 게시글을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "내가 작성한 커뮤니티 게시글 성공적 조회"),
    })
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호(0부터 시작)", in = ParameterIn.QUERY),
            @Parameter(name = "direction", description = "내림차순과 오름차순(desc,asc)", in = ParameterIn.QUERY),
            @Parameter(name = "sort", description = "정렬기준(createdAt)", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "페이지당 아이템 갯수", in = ParameterIn.QUERY)
    })
    public ResponseEntity<BaseResponse<CommunityPostSearchWithSliceResponseDto>> showMyCommunityPost
            (@CurrentMemberLoginId final String loginId,
                    @PageableDefault(size = 10,sort = "createdAt",direction = Direction.DESC,page = 0) final Pageable pageable
            ){


        return ResponseEntity.ok(BaseResponse.createSuccess(communityPostSearchService.searchMyCommunityPost(loginId,pageable)));
    }

}
