package com.cre8.workfieldtag.service;


import com.cre8.response.error.ErrorCode;
import com.cre8.response.error.exception.DuplicateException;
import com.cre8.response.error.exception.NotFoundException;
import com.cre8.workfieldtag.dto.request.WorkFieldChildTagSaveRequestDto;
import com.cre8.workfieldtag.dto.request.WorkFieldChildTagsSaveRequestDto;
import com.cre8.workfieldtag.entity.WorkFieldChildTag;
import com.cre8.workfieldtag.entity.WorkFieldSubCategory;
import com.cre8.workfieldtag.repository.WorkFieldChildTagRepository;
import com.cre8.workfieldtag.repository.WorkFieldSubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkFieldChildTagCDService {

    private final WorkFieldChildTagRepository workFieldChildTagRepository;
    private final WorkFieldSubCategoryRepository workFieldSubCategoryRepository;

    @Transactional
    public void saveWorkFieldChildTag(
            WorkFieldChildTagSaveRequestDto workFieldChildTagSaveRequestDto){

        WorkFieldSubCategory workFieldSubCategory = findWorkFieldSubCategoryById(workFieldChildTagSaveRequestDto.getWorkFieldSubCategoryId());

        checkDuplicateWorkFieldChildTagNameAndWorkFieldSubCategory(
                workFieldChildTagSaveRequestDto.getName(), workFieldSubCategory);

        workFieldSubCategory.addWorkFieldChildTag(workFieldChildTagSaveRequestDto.getName());


    }

    @Transactional
    public void saveWorkFieldChildTags(
            WorkFieldChildTagsSaveRequestDto workFieldChildTagsSaveRequestDto){

        WorkFieldSubCategory workFieldSubCategory = findWorkFieldSubCategoryById(workFieldChildTagsSaveRequestDto.getWorkFieldSubCategoryId());

        workFieldChildTagsSaveRequestDto.getName().forEach(name->{

            checkDuplicateWorkFieldChildTagNameAndWorkFieldSubCategory(
                    name, workFieldSubCategory);

            workFieldSubCategory.addWorkFieldChildTag(name);

        });



    }

    @Transactional
    public void deleteWorkFieldChildTagById(Long workFieldChildTagId){

        WorkFieldChildTag workFieldChildTag = workFieldChildTagRepository.findById(workFieldChildTagId)
                .orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_WORK_FIELD_CHILD_TAG));

        workFieldChildTagRepository.delete(workFieldChildTag);

    }


    private void checkDuplicateWorkFieldChildTagNameAndWorkFieldSubCategory(String name,
            WorkFieldSubCategory workFieldSubCategory){

        if(workFieldChildTagRepository.existsByNameAndWorkFieldSubCategory(name,workFieldSubCategory)){
            throw new DuplicateException(ErrorCode.DUPLICATE_WORK_FIELD_CHILD_NAME);
        }
    }

    private WorkFieldSubCategory findWorkFieldSubCategoryById(Long id){

        return workFieldSubCategoryRepository.findById(id).orElseThrow(()-> new NotFoundException(ErrorCode.CANT_FIND_WORK_FIELD_SUB_CATEGORY));
    }
}
