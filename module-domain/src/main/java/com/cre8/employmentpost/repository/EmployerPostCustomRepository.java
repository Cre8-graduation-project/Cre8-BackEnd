package com.cre8.employmentpost.repository;


import com.cre8.employmentpost.dto.EmployerSearchDBResponseDto;
import com.cre8.employmentpost.entity.EmployerPost;
import com.cre8.employmentpost.search.EmployerPostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployerPostCustomRepository {

    Page<EmployerPost> showEmployerPostListWithPage(final EmployerPostSearch employerPostSearch,final Pageable pageable);

    Page<EmployerSearchDBResponseDto> showEmployerPostDtoListWithPage(final EmployerPostSearch employerPostSearch,final Pageable pageable);
}
