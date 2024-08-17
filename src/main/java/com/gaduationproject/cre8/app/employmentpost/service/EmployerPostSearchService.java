package com.gaduationproject.cre8.app.employmentpost.service;

import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostSearchWithSliceResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployerPostSearch;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostSearchResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostSearchWithCountResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployerPostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmployerPostSearchService {

    private final EmployerPostRepository employerPostRepository;




    public EmployerPostSearchWithCountResponseDto searchEmployerPost(final EmployerPostSearch employerPostSearch,
            final Pageable pageable){

        Page<EmployerPost> employerPostSearchResponseDtoPage =
                employerPostRepository.showEmployerPostListWithPage(employerPostSearch,pageable);

        return EmployerPostSearchWithCountResponseDto.of(employerPostSearchResponseDtoPage.getTotalElements(),
                employerPostSearchResponseDtoPage.getContent().stream().map(employerPost -> {
                    List<String> tagNameList = new ArrayList<>();

                    if(employerPost.getBasicPostContent().getWorkFieldTag()!=null){
                        tagNameList.add(employerPost.getBasicPostContent().getWorkFieldTag().getName());
                    }

                    employerPost.getEmployerPostWorkFieldChildTagList().forEach(employerPostWorkFieldChildTag -> {
                        tagNameList.add(employerPostWorkFieldChildTag.getWorkFieldChildTag().getName());
                    });

                    return EmployerPostSearchResponseDto.of(employerPost,tagNameList);

                }).collect(
                        Collectors.toList()),employerPostSearchResponseDtoPage.getTotalPages());
    }

    public EmployerPostSearchWithSliceResponseDto searchEmployerPostByKeyWord(final String keyword,
            final Pageable pageable) {

        Slice<EmployerPost> employerPostSlice =
                employerPostRepository.findEmployerPostWithFetchMemberAndWorkFieldTagAndChildTagListWithSlice(
                        keyword, pageable);

        return EmployerPostSearchWithSliceResponseDto.of(
                employerPostSlice.stream().map(employerPost -> {
                    List<String> tagNameList = new ArrayList<>();

                    if (employerPost.getBasicPostContent().getWorkFieldTag() != null) {
                        tagNameList.add(
                                employerPost.getBasicPostContent().getWorkFieldTag().getName());
                    }

                    employerPost.getEmployerPostWorkFieldChildTagList()
                            .forEach(employerPostWorkFieldChildTag -> {
                                tagNameList.add(employerPostWorkFieldChildTag.getWorkFieldChildTag()
                                        .getName());
                            });

                    return EmployerPostSearchResponseDto.of(employerPost, tagNameList);
                }).collect(Collectors.toList()), employerPostSlice.hasNext());
    }




}
