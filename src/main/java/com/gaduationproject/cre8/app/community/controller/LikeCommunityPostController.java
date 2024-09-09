package com.gaduationproject.cre8.app.community.controller;

import com.gaduationproject.cre8.app.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.app.community.service.LikeCommunityPostService;
import com.gaduationproject.cre8.app.employmentpost.service.BookMarkEmployeePostService;
import com.gaduationproject.cre8.app.employmentpost.service.BookMarkEmployerPostService;
import com.gaduationproject.cre8.domain.community.repository.LikeCommunityPostRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like/community/posts")
@Tag(name = "커뮤니티 게시글 좋아요 컨트롤러", description = "커뮤니티 게시글 좋아요를 누른다. ")
public class LikeCommunityPostController {

    private final LikeCommunityPostService likeCommunityPostService;


    @PostMapping("{postId}")
    @Operation(summary = "커뮤니티 게시글 좋아요 및 취소하기", description = "커뮤니티  게시글을 좋아요")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "커뮤니티 게시글 성공적 좋아요 및 취소"),
    })
    @Parameters({
            @Parameter(name = "postId", description = "커뮤니티 게시글 아이디", in = ParameterIn.PATH),
    })
    public ResponseEntity<Void> likeCommunityPost(@CurrentMemberLoginId final String loginId,
            @PathVariable(value = "postId") final Long communityPostId) {

        likeCommunityPostService.likeCommunityPost(loginId, communityPostId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
