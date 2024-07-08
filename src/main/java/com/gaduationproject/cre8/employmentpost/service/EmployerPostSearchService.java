package com.gaduationproject.cre8.employmentpost.service;

import com.gaduationproject.cre8.employmentpost.dto.request.EmployerPostSearch;
import com.gaduationproject.cre8.employmentpost.dto.response.EmployerPostSearchResponseDto;
import com.gaduationproject.cre8.employmentpost.repository.EmployerPostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmployerPostSearchService {

    private final EmployerPostRepository employerPostRepository;



    @Transactional
    public List<EmployerPostSearchResponseDto> searchEmployerPost(EmployerPostSearch employerPostSearch,
            Pageable pageable){
        return employerPostRepository.showEmployerPostListWithPage(employerPostSearch,pageable).stream().map(EmployerPostSearchResponseDto::of).collect(
                Collectors.toList());
    }



}
