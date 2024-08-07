package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPostWorkFieldChildTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerPostWorkFieldChildTagRepository extends JpaRepository<EmployerPostWorkFieldChildTag,Long> {

    void deleteByEmployerPost(final EmployerPost employerPost);

}
