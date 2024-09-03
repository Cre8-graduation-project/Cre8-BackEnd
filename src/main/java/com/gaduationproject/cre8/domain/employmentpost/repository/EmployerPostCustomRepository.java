package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployerSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployeePostSearch;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployerPostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployerPostCustomRepository {

    Page<EmployerPost> showEmployerPostListWithPage(final EmployerPostSearch employerPostSearch,final Pageable pageable);

    Page<EmployerSearchDBResponseDto> showEmployerPostDtoListWithPage(final EmployerPostSearch employerPostSearch,final Pageable pageable);
}
