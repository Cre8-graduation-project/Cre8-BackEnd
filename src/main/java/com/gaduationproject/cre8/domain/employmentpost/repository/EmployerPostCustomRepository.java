package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.app.employmentpost.dto.request.EmployerPostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployerPostCustomRepository {

    Page<EmployerPost> showEmployerPostListWithPage(final EmployerPostSearch employerPostSearch,final Pageable pageable);
}
