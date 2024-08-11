package com.gaduationproject.cre8.adminapp.workfieldtag.controller;

import com.gaduationproject.cre8.adminapp.workfieldtag.dto.request.WorkFieldChildTagSaveRequestDto;
import com.gaduationproject.cre8.adminapp.workfieldtag.dto.request.WorkFieldChildTagsSaveRequestDto;
import com.gaduationproject.cre8.adminapp.workfieldtag.dto.request.WorkFieldSubCategoriesSaveRequestDto;
import com.gaduationproject.cre8.adminapp.workfieldtag.dto.request.WorkFieldSubCategorySaveRequestDto;
import com.gaduationproject.cre8.adminapp.workfieldtag.dto.request.WorkFieldTagSaveRequestDto;
import com.gaduationproject.cre8.adminapp.workfieldtag.service.WorkFieldChildTagCDService;
import com.gaduationproject.cre8.adminapp.workfieldtag.service.WorkFieldSubCategoryCDService;
import com.gaduationproject.cre8.adminapp.workfieldtag.service.WorkFieldTagCDService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
@Tag(name = "태그 저장 삭제용 컨트롤러", description = "태그를 저장하고 삭제하는 용도의 컨트롤러입니다. ")
public class WorkFieldTagCDController {

    private final WorkFieldTagCDService workFieldTagCDService;
    private final WorkFieldSubCategoryCDService workFieldSubCategoryCDService;
    private final WorkFieldChildTagCDService workFieldChildTagCDService;


    @PostMapping
    @Operation(summary = "작업 태그 저장",description = "맨 위에 상위 작업 태그를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "상위 작업 태그 저장 성공"),
            @ApiResponse(responseCode = "409",description = "같은 이름의 상위 작업 태그 이름이 이미 있는 경우")
    })
    public ResponseEntity<Void> saveWorkFieldTag(@Valid @RequestBody WorkFieldTagSaveRequestDto workFieldTagSaveRequestDto){

        workFieldTagCDService.saveWorkFieldTag(workFieldTagSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/{workFieldTagId}")
    @Operation(summary = "작업 태그 삭제",description = "맨 위에 상위 작업 태그를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "태그 삭제 성공"),
            @ApiResponse(responseCode = "404",description = "ID 기반으로 작업태그를 찾이 못한 경우")
    })
    public ResponseEntity<Void> deleteWorkFieldTag(@PathVariable("workFieldTagId") Long workFieldTagId){

        workFieldTagCDService.deleteWorkFieldTagByWorkFieldId(workFieldTagId);

        return ResponseEntity.ok().build();

    }

    @PostMapping("/category")
    @Operation(summary = "작업 태그의 카테고리 생성",description = "작업 태그의 카테고리를 저장합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "ID 기반으로 작업태그를 찾지 못한 경우"),
            @ApiResponse(responseCode = "409",description = "같은 작업 태그 내에 같은 카테고리 이름이 존재할 경우"),
            @ApiResponse(responseCode = "200",description = "성공적 카테고리 생성")
    })
    public ResponseEntity<Void> saveWorkFieldSubCategory(@Valid @RequestBody
            WorkFieldSubCategorySaveRequestDto workFieldSubCategorySaveRequestDto){

        workFieldSubCategoryCDService.saveWorkFieldSubCategory(workFieldSubCategorySaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/categories")
    @Operation(summary = "작업 태그의 카테고리 생성",description = "작업 태그의 카테고리를 저장합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "ID 기반으로 작업태그를 찾지 못한 경우"),
            @ApiResponse(responseCode = "409",description = "같은 작업 태그 내에 같은 카테고리 이름이 존재할 경우"),
            @ApiResponse(responseCode = "200",description = "성공적 카테고리 생성")
    })
    public ResponseEntity<Void> saveWorkFieldSubCategories(@Valid @RequestBody
    WorkFieldSubCategoriesSaveRequestDto workFieldSubCategoriesSaveRequestDto){

        workFieldSubCategoryCDService.saveWorkFieldSubCategories(workFieldSubCategoriesSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/category/{workFieldSubCategoryId}")
    @Operation(summary = "작업 태그의 카테고리 삭제",description = "작업 태그의 카테고리를 삭제합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "ID 기반으로 카테고리를 찾지 못한 경우"),
            @ApiResponse(responseCode = "200",description = "성공적 카테고리 삭제")
    })
    public ResponseEntity<Void> deleteWorkFieldSubCategory(@PathVariable("workFieldSubCategoryId")
    Long workFieldSubCategoryId){

        workFieldSubCategoryCDService.deleteWorkFieldSubCategoryById(workFieldSubCategoryId);

        return ResponseEntity.ok().build();

    }

    @PostMapping("/child")
    @Operation(summary = "카테고리의 하위 태그 생성",description = "카테고리의 하위 태그를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "ID 기반으로 카테고리를 찾지 못한 경우"),
            @ApiResponse(responseCode = "409",description = "같은 카테고리 내에 같은 이름의 태그가 존재할 경우"),
            @ApiResponse(responseCode = "200",description = "성공적 카테고리내에 태그 생성")
    })
    public ResponseEntity<Void> saveWorkFieldChildTag(@Valid @RequestBody
    WorkFieldChildTagSaveRequestDto workFieldChildTagSaveRequestDto){

        workFieldChildTagCDService.saveWorkFieldChildTag(workFieldChildTagSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/childs")
    @Operation(summary = "카테고리의 하위 태그 생성",description = "카테고리의 하위 태그를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "ID 기반으로 카테고리를 찾지 못한 경우"),
            @ApiResponse(responseCode = "409",description = "같은 카테고리 내에 같은 이름의 태그가 존재할 경우"),
            @ApiResponse(responseCode = "200",description = "성공적 카테고리내에 태그 생성")
    })
    public ResponseEntity<Void> saveWorkFieldChildTags(@Valid @RequestBody
    WorkFieldChildTagsSaveRequestDto workFieldChildTagsSaveRequestDto){

        workFieldChildTagCDService.saveWorkFieldChildTags(workFieldChildTagsSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/child/{workFieldChildTagId}")
    @Operation(summary = "카테고리의 하위 태그 삭제",description = "카테고리의 하위 태그를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "ID 기반으로 하위 태그를 찾지 못한 경우"),
            @ApiResponse(responseCode = "200",description = "성공적 카테고리를 삭제할 경우")
    })
    public ResponseEntity<Void> deleteWorkFieldChildTag(@PathVariable("workFieldChildTagId")
    Long workFieldChildTagId){

        workFieldChildTagCDService.deleteWorkFieldChildTagById(workFieldChildTagId);

        return ResponseEntity.ok().build();

    }






}
