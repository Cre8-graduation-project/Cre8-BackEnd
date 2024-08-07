package com.gaduationproject.cre8.api.workfieldtag.service;

import com.gaduationproject.cre8.api.workfieldtag.dto.response.WorkFieldChildTagResponseDto;
import com.gaduationproject.cre8.api.workfieldtag.dto.response.WorkFieldChildTagWithSubCategoryNameResponseDto;
import com.gaduationproject.cre8.api.workfieldtag.dto.response.WorkFieldSubCategoryResponseDto;
import com.gaduationproject.cre8.api.workfieldtag.dto.response.WorkFieldTagResponseDto;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldSubCategory;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldSubCategoryRepository;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldTagRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkFieldTagShowService {

    private final WorkFieldTagRepository workFieldTagRepository;
    private final WorkFieldSubCategoryRepository workFieldSubCategoryRepository;

    public List<WorkFieldTagResponseDto> showAllWorkFieldTag(){

        return workFieldTagRepository.findAll().stream().map(WorkFieldTagResponseDto::of).collect(
                Collectors.toList());

    }

    public List<WorkFieldSubCategoryResponseDto> showAllWorkFieldSubCategoryByWorkFieldId(Long workFieldId){

        return workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldId).stream().map(WorkFieldSubCategoryResponseDto::from).collect(
                Collectors.toList());

    }

    public List<WorkFieldChildTagWithSubCategoryNameResponseDto> showAllChildTagByWorkFieldId(Long workFieldId){

        List<WorkFieldSubCategory> workFieldSubCategoryList =
                workFieldSubCategoryRepository.findByWorkFieldTagWithFetchWorkFieldChildTagList(workFieldId);


        return workFieldSubCategoryList.stream().map(workFieldSubCategory -> {
            return WorkFieldChildTagWithSubCategoryNameResponseDto.of(workFieldSubCategory.getName(),workFieldSubCategory.getWorkFieldChildTagList()
                    .stream().map(WorkFieldChildTagResponseDto::from).collect(Collectors.toList()));
        }).collect(Collectors.toList());

    }

}
