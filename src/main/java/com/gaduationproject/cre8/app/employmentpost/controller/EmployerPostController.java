package com.gaduationproject.cre8.app.employmentpost.controller;

import com.gaduationproject.cre8.app.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.app.response.BaseResponse;
import com.gaduationproject.cre8.app.employmentpost.dto.request.EditEmployerPostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.request.SaveEmployerPostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostResponseDto;
import com.gaduationproject.cre8.app.employmentpost.service.EmployerPostCRUDService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employer/posts")
@Tag(name = "구인자 게시글 관련 컨트롤러", description = "구인자 게시글을 단건 조회, 수정, 삭제 ,생성합니다 ")
public class EmployerPostController {

    private final EmployerPostCRUDService employerPostCRUDService;

    @PostMapping
    @Operation(summary = "구인자 게시글 생성",description = "구인자가 게시글을 생성합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "구인자 게시글 성공적 생성"),
            @ApiResponse(responseCode = "404",description = "부모 태그, 하위 태그를 찾지 못하였을때 "),
            @ApiResponse(responseCode = "400",description = "하위 태그와 부모 태그가 같지 않을 때")

    })
    public ResponseEntity<Void> saveEmployerPost(@CurrentMemberLoginId String loginId, @RequestBody @Valid SaveEmployerPostRequestDto saveEmployerPostRequestDto){

        employerPostCRUDService.saveEmployerPost(loginId, saveEmployerPostRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/{postId}")
    @Operation(summary = "구인자 게시글 단건 조회",description = "구인자가 게시글을 Id 기반으로 단건 조회합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "구인자 게시글 성공적 단건 조회"),
            @ApiResponse(responseCode = "404",description = "ID로 구인자 게시글을 찾지 못했을 때")

    })
    public ResponseEntity<BaseResponse<EmployerPostResponseDto>> showEmployerPost(@PathVariable("postId") Long postId){

        return ResponseEntity.ok(BaseResponse.createSuccess(employerPostCRUDService.showEmployerPost(postId)));

    }

    @PutMapping
    @Operation(summary = "구인자 게시글 수정",description = "구인자가 게시글을 Id 기반으로 수정합니다 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "구인자 게시글 성공적 수정"),
            @ApiResponse(responseCode = "400",description = "다른 사람의 구인자 게시글을 수정할 때, 하위 태그의 부모가 상위 태그랑 같지 않을 때"),
            @ApiResponse(responseCode = "404",description = "ID 기반으로 구인자 게시글을 못찾을 때, 작업 태그를 찾지 못할 때 ")

    })
    public ResponseEntity<Void> editEmployerPost(@CurrentMemberLoginId String longinId,
            @RequestBody @Valid EditEmployerPostRequestDto editEmployerPostRequestDto){

        employerPostCRUDService.updateEmployerPost(longinId,editEmployerPostRequestDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "구인자 게시글 삭제",description = "구인자가 게시글을 Id 기반으로 삭제합니다 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "구인자 게시글 성공적 삭제"),
            @ApiResponse(responseCode = "400",description = "다른 사람의 구인자 게시글을 삭제할 때"),
            @ApiResponse(responseCode = "404",description = "ID 기반으로 구인자 게시글을 찾지 못할 때")

    })
    public ResponseEntity<Void> deleteEmployerPost(@CurrentMemberLoginId String loginId, @PathVariable("postId") Long employerPostId){

        employerPostCRUDService.deleteEmployerPost(loginId,employerPostId);

        return ResponseEntity.ok().build();
    }

}
