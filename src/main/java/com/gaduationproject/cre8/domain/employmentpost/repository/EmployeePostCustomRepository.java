package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchResponseDto2;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchResponseDto3;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployeePostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeePostCustomRepository {
    Page<EmployeePost> showEmployeePostListWithPage(final EmployeePostSearch employeePostSearch,final Pageable pageable);

    Page<EmployeeSearchResponseDto> testShowEmployeePostListWithPage(final EmployeePostSearch employeePostSearch,final Pageable pageable);

    Page<EmployeeSearchResponseDto2> testShowEmployeePostListWithPage2(final EmployeePostSearch employeePostSearch,final Pageable pageable);

    Page<EmployeeSearchResponseDto3> testShowEmployeePostListWithPage3(final EmployeePostSearch employeePostSearch,final Pageable pageable);

}
