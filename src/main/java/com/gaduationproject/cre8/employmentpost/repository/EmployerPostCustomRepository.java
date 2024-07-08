package com.gaduationproject.cre8.employmentpost.repository;

import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPost;
import com.gaduationproject.cre8.employmentpost.dto.request.EmployerPostSearch;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface EmployerPostCustomRepository {

    List<EmployerPost> showEmployerPostListWithPage(EmployerPostSearch employerPostSearch, Pageable pageable);
}
