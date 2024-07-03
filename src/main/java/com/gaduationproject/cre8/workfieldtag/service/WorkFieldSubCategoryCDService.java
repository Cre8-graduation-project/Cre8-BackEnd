package com.gaduationproject.cre8.workfieldtag.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.DuplicateException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.workfieldtag.dto.request.WorkFieldSubCategoriesSaveRequestDto;
import com.gaduationproject.cre8.workfieldtag.dto.request.WorkFieldSubCategorySaveRequestDto;
import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldSubCategory;
import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldTag;
import com.gaduationproject.cre8.workfieldtag.repository.WorkFieldSubCategoryRepository;
import com.gaduationproject.cre8.workfieldtag.repository.WorkFieldTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkFieldSubCategoryCDService {

    private final WorkFieldSubCategoryRepository workFieldSubCategoryRepository;
    private final WorkFieldTagRepository workFieldTagRepository;


    @Transactional
    public void saveWorkFieldSubCategory(WorkFieldSubCategorySaveRequestDto workFieldSubCategorySaveRequestDto){

       WorkFieldTag workFieldTag = findWorkFieldTagById(workFieldSubCategorySaveRequestDto.getWorkFieldId());

       checkDuplicateWorkSubCategoryNameAndWorkFieldTag(workFieldSubCategorySaveRequestDto.getName(),workFieldTag);

       workFieldTag.addWorkFieldSubCategory(workFieldSubCategorySaveRequestDto.getName());


    }

    @Transactional
    public void saveWorkFieldSubCategories(WorkFieldSubCategoriesSaveRequestDto workFieldSubCategoriesSaveRequestDto){

        WorkFieldTag workFieldTag = findWorkFieldTagById(workFieldSubCategoriesSaveRequestDto.getWorkFieldId());

        workFieldSubCategoriesSaveRequestDto.getName().forEach(name ->{
            checkDuplicateWorkSubCategoryNameAndWorkFieldTag(name,workFieldTag);
            workFieldTag.addWorkFieldSubCategory(name);
        });


    }

    @Transactional
    public void deleteWorkFieldSubCategoryById(Long workFieldSubCategoryId){

        WorkFieldSubCategory workFieldSubCategory = workFieldSubCategoryRepository.findById(workFieldSubCategoryId)
                .orElseThrow(()-> new NotFoundException(ErrorCode.CANT_FIND_WORK_FIELD_SUB_CATEGORY));

        workFieldSubCategoryRepository.delete(workFieldSubCategory);

    }



    private void checkDuplicateWorkSubCategoryNameAndWorkFieldTag(String name,WorkFieldTag workFieldTag){
        if(workFieldSubCategoryRepository.existsByNameAndWorkFieldTag(name,workFieldTag)){
            throw new DuplicateException(ErrorCode.DUPLICATE_SUB_CATEGORY_NAME);
        }
    }

    private WorkFieldTag findWorkFieldTagById(Long id){

        return workFieldTagRepository.findById(id).orElseThrow(()-> new NotFoundException(ErrorCode.CANT_FIND_WORK_FILED_TAG));
    }


}
