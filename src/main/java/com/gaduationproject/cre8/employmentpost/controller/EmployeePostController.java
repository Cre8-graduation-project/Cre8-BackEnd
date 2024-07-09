package com.gaduationproject.cre8.employmentpost.controller;

import com.gaduationproject.cre8.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.common.response.BaseResponse;
import com.gaduationproject.cre8.employmentpost.dto.request.SaveEmployeePostRequestDto;
import com.gaduationproject.cre8.employmentpost.dto.request.SaveEmployerPostRequestDto;
import com.gaduationproject.cre8.employmentpost.dto.response.EmployeePostResponseDto;
import com.gaduationproject.cre8.employmentpost.dto.response.EmployerPostResponseDto;
import com.gaduationproject.cre8.employmentpost.service.EmployeePostCRUDService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee/posts")
@Tag(name = "구직자 게시글 관련 컨트롤러", description = "구직자 게시글을 단건 조회, 수정, 삭제 ,생성합니다 ")
public class EmployeePostController {

    private final EmployeePostCRUDService employeePostCRUDService;

    @PostMapping
    @Operation(summary = "구직자 게시글 생성",description = "구직자가 게시글을 생성합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "구직자 게시글 성공적 생성"),
            @ApiResponse(responseCode = "404",description = "부모 태그, 하위 태그를 찾지 못하였을때 "),
            @ApiResponse(responseCode = "400",description = "하위 태그와 부모 태그가 같지 않을 때")

    })
    public ResponseEntity<Void> saveEmployeePost(@CurrentMemberLoginId final String loginId, @RequestBody @Valid final SaveEmployeePostRequestDto saveEmployeePostRequestDto){

        employeePostCRUDService.saveEmployeePost(loginId, saveEmployeePostRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/{postId}")
    @Operation(summary = "구직자 게시글 단건 조회",description = "구직자의 게시글을 Id 기반으로 단건 조회합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "구직자 게시글 성공적 단건 조회"),
            @ApiResponse(responseCode = "404",description = "ID로 구직자 게시글을 찾지 못했을 때")

    })
    public ResponseEntity<BaseResponse<EmployeePostResponseDto>> showEmployeePost(@PathVariable("postId") Long postId){

        return ResponseEntity.ok(BaseResponse.createSuccess(employeePostCRUDService.showEmployeePost(postId)));

    }


}
