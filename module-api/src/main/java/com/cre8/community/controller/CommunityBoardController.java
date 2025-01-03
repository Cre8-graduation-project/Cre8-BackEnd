package com.cre8.community.controller;

import com.cre8.community.dto.response.CommunityBoardResponseDto;
import com.cre8.community.service.CommunityBoardService;
import com.cre8.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community/boards")
@Tag(name = "커뮤니티 게시판 관련 컨트롤러 ", description = "커뮤니티 게시판을 만들거나 삭제한다.")
public class CommunityBoardController {

    private final CommunityBoardService communityBoardService;



    @GetMapping
    @Operation(summary = "커뮤니티 게시판 리스트 조회",description = "커뮤니티 게시판의 리스트를 조회한다(ex:자유게시판, 비밀게시판)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "커뮤니티 게시판 리스트 성공적 조회")
    })
    public ResponseEntity<BaseResponse<List<CommunityBoardResponseDto>>> communityBoardList(){
        return ResponseEntity.ok(BaseResponse.createSuccess(communityBoardService.findAllCommunityBoard()));
    }



}