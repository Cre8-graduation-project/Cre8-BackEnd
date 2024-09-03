package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployeePostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeePostCustomRepository {
    Page<EmployeePost> showEmployeePostListWithPage(final EmployeePostSearch employeePostSearch,final Pageable pageable);

    Page<EmployeeSearchDBResponseDto> showEmployeePostDtoListWithPage(final EmployeePostSearch employeePostSearch,final Pageable pageable);


}
