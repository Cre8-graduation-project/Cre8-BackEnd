package com.gaduationproject.cre8.employmentpost.controller;

import com.gaduationproject.cre8.employmentpost.dto.request.EmployerPostSearch;
import com.gaduationproject.cre8.employmentpost.dto.response.EmployerPostSearchResponseDto;
import com.gaduationproject.cre8.employmentpost.service.EmployerPostSearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployerPostSearchController {

    private final EmployerPostSearchService employerPostSearchService;


    @GetMapping("/api/v1/employer-post/search")
    public List<EmployerPostSearchResponseDto> employerPostSearchResponse(EmployerPostSearch employerPostSearch,
            Pageable pageable){

        return employerPostSearchService.searchEmployerPost(employerPostSearch,pageable);
    }

}
