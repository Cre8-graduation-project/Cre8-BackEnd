package com.gaduationproject.cre8.app.workfieldtag.controller;

import com.gaduationproject.cre8.common.response.BaseResponse;
import com.gaduationproject.cre8.app.workfieldtag.dto.response.WorkFieldChildTagWithSubCategoryNameResponseDto;
import com.gaduationproject.cre8.app.workfieldtag.dto.response.WorkFieldSubCategoryResponseDto;
import com.gaduationproject.cre8.app.workfieldtag.dto.response.WorkFieldTagResponseDto;
import com.gaduationproject.cre8.app.workfieldtag.service.WorkFieldTagShowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
@Tag(name = "태그 조회용 컨트롤러", description = "포트폴리오나 구인 구직 게시글에 전체 태그 조회시 사용됩니다. ")
public class WorkFieldTagShowController {

    private final WorkFieldTagShowService workFieldTagShowService;

    @GetMapping
    @Operation(summary = "모든 작업 분야 태그 확인",description = "저장된 모든 작업 분야 태그를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "성공적 작업 분야 태그 리스트 조회 성공")
    })
    public ResponseEntity<BaseResponse<List<WorkFieldTagResponseDto>>> showAllWorkFieldTag(){

        return ResponseEntity.ok(BaseResponse.createSuccess(workFieldTagShowService.showAllWorkFieldTag()));
    }

    @GetMapping("/subcategory/{workFieldTagId}")
    @Operation(summary = "작업 분야 태그의 카테고리 확인",description = "작업 분야 태그에 저장된 카테고리를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "성공적 작업 분야 내 카테고리 조회 성공")
    })
    public ResponseEntity<BaseResponse<List<WorkFieldSubCategoryResponseDto>>> showAllWorkFieldSubCategoryByWorkFieldTagId(
            @PathVariable("workFieldTagId") Long workFieldTagId){

        return ResponseEntity.ok(BaseResponse.createSuccess(workFieldTagShowService.showAllWorkFieldSubCategoryByWorkFieldId(workFieldTagId)));
    }

    @GetMapping("/child/{workFieldTagId}")
    @Operation(summary = "상위 작업 분야의 카테고리 이름, 하위 태그 조회",description = "상위 작업 분야 ID기반으로 카테고리 이름과 하위 태그를 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "성공적으로 카테고리 이름, 하위 태그 반환")
    })
    public ResponseEntity<BaseResponse<List<WorkFieldChildTagWithSubCategoryNameResponseDto>>> showAllWorkFieldChildTagWithSubCategoryName(
            @PathVariable("workFieldTagId") Long workFieldTagId){

        return ResponseEntity.ok(BaseResponse.createSuccess(workFieldTagShowService.showAllChildTagByWorkFieldId(workFieldTagId)));
    }

}
