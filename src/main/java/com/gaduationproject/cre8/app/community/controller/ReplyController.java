package com.gaduationproject.cre8.app.community.controller;

import com.gaduationproject.cre8.app.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.app.community.dto.request.CommunityPostSaveRequestDto;
import com.gaduationproject.cre8.app.community.dto.request.ReplyEditRequestDto;
import com.gaduationproject.cre8.app.community.dto.request.ReplySaveRequestDto;
import com.gaduationproject.cre8.app.community.service.ReplyService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community/posts/reply")
@Tag(name = "커뮤니티 게시글 댓글 관련 컨트롤러 ", description = "커뮤니티 게시글 댓글을 생성,수정,삭제 를 진행한다.")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    @Operation(summary = "커뮤니티 게시글 답글  생성",description = "커뮤니티에 게시글에 답글을 생성합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "구직자 게시글 성공적 생성"),
            @ApiResponse(responseCode = "404",description = "로그인한 사용자를 못찾았을 때, 부모 댓글을 찾지 못했을 때, 게시글을 찾지 못했을 때"),
            @ApiResponse(responseCode = "400",description = "대댓글에 댓글을 달려고 할 떄")

    })
    public ResponseEntity<Void> saveCommunityPostReply(@CurrentMemberLoginId final String loginId,
            @Valid @RequestBody final ReplySaveRequestDto replySaveRequestDto){

        replyService.saveReply(replySaveRequestDto, loginId);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PatchMapping
    @Operation(summary = "커뮤니티 게시글 답글 수정",description = "커뮤니티에 게시글에 답글을 수정합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "로그인한 사용자를 못찾았을 때, 댓글을 찾지 못했을 때"),
            @ApiResponse(responseCode = "400",description = "대댓글에 댓글을 달려고 할 떄,자신의 댓글이 아닌 것을 수정, 삭제 했을 때 ")

    })
    public ResponseEntity<Void> updateCommunityPostReply(@CurrentMemberLoginId final String loginId,
            @Valid @RequestBody final ReplyEditRequestDto replyEditRequestDto){

        replyService.updateReply(replyEditRequestDto,loginId);

        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{replyId}")
    @Operation(summary = "커뮤니티 게시글 답글 삭제",description = "커뮤니티에 게시글에 답글을 삭제합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "로그인한 사용자를 못찾았을 때, 댓글을 찾지 못했을 때"),
            @ApiResponse(responseCode = "400",description = "자신의 댓글이 아닌 것을 수정, 삭제 했을 때 "),
    })
    public ResponseEntity<Void> deleteCommunityPostReply(@CurrentMemberLoginId final String loginId,
            @PathVariable("replyId")final Long replyId){

        replyService.deleteReply(replyId,loginId);

        return ResponseEntity.ok().build();

    }
}
