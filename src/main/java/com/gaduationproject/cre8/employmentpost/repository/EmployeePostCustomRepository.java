package com.gaduationproject.cre8.employmentpost.repository;

import com.gaduationproject.cre8.employmentpost.domain.entity.EmployeePost;
import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPost;
import com.gaduationproject.cre8.employmentpost.dto.request.EmployeePostSearch;
import com.gaduationproject.cre8.employmentpost.dto.request.EmployerPostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeePostCustomRepository {
    Page<EmployeePost> showEmployeePostListWithPage(final EmployeePostSearch employeePostSearch,final Pageable pageable);

}
