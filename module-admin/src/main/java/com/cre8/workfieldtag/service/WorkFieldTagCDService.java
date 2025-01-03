package com.cre8.workfieldtag.service;


import com.cre8.response.error.ErrorCode;
import com.cre8.response.error.exception.DuplicateException;
import com.cre8.response.error.exception.NotFoundException;
import com.cre8.workfieldtag.dto.request.WorkFieldTagSaveRequestDto;
import com.cre8.workfieldtag.entity.WorkFieldTag;
import com.cre8.workfieldtag.repository.WorkFieldTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkFieldTagCDService {

    private final WorkFieldTagRepository workFieldTagRepository;



    @Transactional
    public void saveWorkFieldTag(WorkFieldTagSaveRequestDto workFieldTagSaveRequestDto){

        checkDuplicateWorkFieldName(workFieldTagSaveRequestDto.getName());

        workFieldTagRepository.save(WorkFieldTag.builder()
                .name(workFieldTagSaveRequestDto.getName())
                .build());

    }

    @Transactional
    public void deleteWorkFieldTagByWorkFieldId(Long workFieldTagId){

        WorkFieldTag workFieldTag = workFieldTagRepository.findById(workFieldTagId)
                .orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_WORK_FILED_TAG));

        workFieldTagRepository.delete(workFieldTag);
    }



    private void checkDuplicateWorkFieldName(String name){
        if(workFieldTagRepository.existsByName(name)){
            throw new DuplicateException(ErrorCode.DUPLICATE_WORK_FIELD_NAME);
        }
    }

}
