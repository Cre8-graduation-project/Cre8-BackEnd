package com.cre8.workfieldtag.service;


import com.cre8.workfieldtag.dto.response.WorkFieldChildTagResponseDto;
import com.cre8.workfieldtag.dto.response.WorkFieldChildTagWithSubCategoryNameResponseDto;
import com.cre8.workfieldtag.dto.response.WorkFieldSubCategoryResponseDto;
import com.cre8.workfieldtag.dto.response.WorkFieldTagResponseDto;
import com.cre8.workfieldtag.entity.WorkFieldSubCategory;
import com.cre8.workfieldtag.repository.WorkFieldSubCategoryRepository;
import com.cre8.workfieldtag.repository.WorkFieldTagRepository;
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
