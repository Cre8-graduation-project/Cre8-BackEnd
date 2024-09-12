package com.gaduationproject.cre8.app.community.controller;

import com.gaduationproject.cre8.app.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.app.community.dto.request.CommunityPostEditRequestDto;
import com.gaduationproject.cre8.app.community.dto.request.CommunityPostSaveRequestDto;
import com.gaduationproject.cre8.app.community.dto.response.CommunityPostResponseDto;
import com.gaduationproject.cre8.app.community.service.CommunityPostCRUDService;
import com.gaduationproject.cre8.app.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/community/posts")
@Tag(name = "커뮤니티 게시글 관련  컨트롤러 ", description = "커뮤니티 게시글을 생성, 읽기, 수정 , 삭제 를 진행한다.")
public class CommunityPostController {


    private final CommunityPostCRUDService communityPostCRUDService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "커뮤니티 게시글 생성",description = "커뮤니티에 게시글을 생성합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "구직자 게시글 성공적 생성"),
            @ApiResponse(responseCode = "404",description = "로그인한 사용자를 못찾았을 때 ")

    })
    public ResponseEntity<Void> saveCommunityPost(@CurrentMemberLoginId final String loginId,
                                                  @Valid @ModelAttribute final CommunityPostSaveRequestDto communityPostSaveRequestDto) throws InterruptedException{

        communityPostCRUDService.saveCommunityPost(loginId, communityPostSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/{postId}")
    @Operation(summary = "커뮤니티 게시글 단건 조회",description = "커뮤니티의 게시글을 Id 기반으로 단건 조회합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "커뮤니티 게시글 성공적 단건 조회"),
            @ApiResponse(responseCode = "404",description = "ID로 커뮤니티 게시글을 찾지 못했을 때")

    })
    public ResponseEntity<BaseResponse<CommunityPostResponseDto>> showCommunityPost(@PathVariable("postId") final Long postId,
                                                                                    @CurrentMemberLoginId final String loginId){

        return ResponseEntity.ok(BaseResponse.createSuccess(communityPostCRUDService.showCommunityPost(postId,loginId)));

    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "커뮤니티 게시글 수정",description = "커뮤니티 게시글을 Id 기반으로 수정합니다 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "커뮤니티 게시글 성공적 수정"),
            @ApiResponse(responseCode = "400",description = "수정 권한이 존재하지 않을 때 "),
            @ApiResponse(responseCode = "404",description = "ID 기반으로 커뮤니티 게시글을 못찾을 때")

    })
    public ResponseEntity<Void> editCommunityPost(@CurrentMemberLoginId final String longinId,
            @ModelAttribute @Valid CommunityPostEditRequestDto communityPostEditRequestDto) throws InterruptedException{


        communityPostCRUDService.updateCommunityPost(longinId,communityPostEditRequestDto);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{postId}")
    @Operation(summary = "커뮤니티 게시글 삭제",description = "커뮤니티 게시글을 Id 기반으로 삭제합니다 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "커뮤니티 게시글 성공적 삭제"),
            @ApiResponse(responseCode = "400",description = "삭제 권한이 존재하지 않을 때 "),
            @ApiResponse(responseCode = "404",description = "ID 기반으로 커뮤니티 게시글을 찾지 못할 때")

    })
    public ResponseEntity<Void> deleteCommunityPost(@CurrentMemberLoginId String loginId, @PathVariable("postId") Long communityPostId){

        communityPostCRUDService.deleteCommunityPost(loginId,communityPostId);

        return ResponseEntity.ok().build();
    }

}
