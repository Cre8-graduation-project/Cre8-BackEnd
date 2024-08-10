package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.app.employmentpost.dto.request.EmployeePostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeePostCustomRepository {
    Page<EmployeePost> showEmployeePostListWithPage(final EmployeePostSearch employeePostSearch,final Pageable pageable);

}
