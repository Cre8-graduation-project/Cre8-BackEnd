package com.gaduationproject.cre8.app.employmentpost.controller;

import com.gaduationproject.cre8.app.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.app.employmentpost.service.BookMarkEmployeePostService;
import com.gaduationproject.cre8.app.employmentpost.service.BookMarkEmployerPostService;
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
@RequestMapping("/api/v1/bookmark")
@Tag(name = "구직자,구인자 게시글 찜하기 컨트롤러", description = "구직자,구인자 게시글을 찜하거나 취소한다.")
public class BookMarkEmploymentController {

    private final BookMarkEmployeePostService bookMarkEmployeePostService;
    private final BookMarkEmployerPostService bookMarkEmployerPostService;


    @PostMapping("/employee-post/{postId}")
    @Operation(summary = "구직자 게시글 찜하기 및 취소하기", description = "구직자 게시글을 찜")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "구직자 게시글 성공적 찜"),
    })
    @Parameters({
            @Parameter(name = "postId",description = "구직자 게시글 아이디",in = ParameterIn.PATH),
    })
    public ResponseEntity<Void>  bookMarkEmployeePost(@CurrentMemberLoginId final String loginId,
            @PathVariable(value = "postId") final Long employeePostId){

        bookMarkEmployeePostService.bookMarkPost(loginId,employeePostId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PostMapping("/employer-post/{postId}")
    @Operation(summary = "구인자 게시글 찜하기 및 취소하기 ", description = "구인자 게시글을 찜")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "구인자 게시글 성공적 찜"),
    })
    @Parameters({
            @Parameter(name = "postId",description = "구인자 게시글 아이디",in = ParameterIn.PATH),
    })
    public ResponseEntity<Void>  bookMarkEmployerPost(@CurrentMemberLoginId final String loginId,
            @PathVariable(value = "postId") final Long employerPostId){

        bookMarkEmployerPostService.bookMarkPost(loginId,employerPostId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
