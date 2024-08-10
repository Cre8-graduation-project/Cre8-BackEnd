package com.gaduationproject.cre8.app.employmentpost.service;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.app.employmentpost.dto.request.EmployeePostSearch;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchWithCountResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployeePostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmployeePostSearchService {

    private final EmployeePostRepository employeePostRepository;




    public EmployeePostSearchWithCountResponseDto searchEmployeePost(final EmployeePostSearch employerPostSearch,
            final Pageable pageable){

        Page<EmployeePost> employeePostSearchResponseDtoPage =
                employeePostRepository.showEmployeePostListWithPage(employerPostSearch,pageable);

        return EmployeePostSearchWithCountResponseDto.of(employeePostSearchResponseDtoPage.getTotalElements(),
                employeePostSearchResponseDtoPage.getContent().stream().map(employeePost -> {
                    List<String> tagNameList = new ArrayList<>();

                    if(employeePost.getBasicPostContent().getWorkFieldTag()!=null){
                        tagNameList.add(employeePost.getBasicPostContent().getWorkFieldTag().getName());
                    }

                    employeePost.getEmployeePostWorkFieldChildTagList().forEach(employeePostWorkFieldChildTag -> {
                        tagNameList.add(employeePostWorkFieldChildTag.getWorkFieldChildTag().getName());
                    });

                    return EmployeePostSearchResponseDto.of(employeePost,tagNameList);

                }).collect(
                        Collectors.toList()),employeePostSearchResponseDtoPage.getTotalPages());
    }

}
