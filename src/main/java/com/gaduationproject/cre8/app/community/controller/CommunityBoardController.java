package com.gaduationproject.cre8.app.community.controller;

import com.gaduationproject.cre8.app.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.app.community.dto.request.CommunityBoardSaveRequestDto;
import com.gaduationproject.cre8.app.community.dto.response.CommunityBoardResponseDto;
import com.gaduationproject.cre8.app.community.service.CommunityBoardService;
import com.gaduationproject.cre8.domain.community.entity.CommunityBoard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community/boards")
@Tag(name = "커뮤니티 게시판 관련 컨트롤러 ", description = "커뮤니티 게시판을 만들거나 삭제한다.")
public class CommunityBoardController {

    private final CommunityBoardService communityBoardService;


    @PostMapping
    @Operation(summary = "커뮤니티 게시판 생성",description = "커뮤니티 게시판을 생성한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "커뮤니티 게시판 생성")
    })
    public ResponseEntity<Void> saveCommunityBoard(@Valid @RequestBody final CommunityBoardSaveRequestDto communityBoardSaveRequestDto,
                                   @CurrentMemberLoginId String loginId){

        communityBoardService.saveCommunityBoard(communityBoardSaveRequestDto,loginId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{communityBoardId}")
    @Operation(summary = "커뮤니티 게시판 삭제",description = "커뮤니티 게시판을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "커뮤니티 게시판 성공적 삭제"),
            @ApiResponse(responseCode = "404",description = "커뮤니티 게시판 ID 기반으로 찾기 실패")
    })
    public ResponseEntity<Void> deleteCommunityBoard(@PathVariable("communityBoardId") final Long communityBoardId){

        communityBoardService.deleteCommunityBoard(communityBoardId);

        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping
    @Operation(summary = "커뮤니티 게시판 리스트 조회",description = "커뮤니티 게시판의 리스트를 조회한다(ex:자유게시판, 비밀게시판)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "커뮤니티 게시판 리스트 성공적 조회")
    })
    public ResponseEntity<List<CommunityBoardResponseDto>> communityBoardList(){
        return ResponseEntity.ok(communityBoardService.findAllCommunityBoard());
    }



}
