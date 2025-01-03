package com.cre8.employmentpost.repository;


import com.cre8.employmentpost.dto.EmployeeSearchDBResponseDto;
import com.cre8.employmentpost.entity.EmployeePost;
import com.cre8.employmentpost.search.EmployeePostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeePostCustomRepository {
    Page<EmployeePost> showEmployeePostListWithPage(final EmployeePostSearch employeePostSearch,final Pageable pageable);

    Page<EmployeeSearchDBResponseDto> showEmployeePostDtoListWithPage(final EmployeePostSearch employeePostSearch,final Pageable pageable);


}
