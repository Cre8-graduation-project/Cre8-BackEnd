package com.gaduationproject.cre8.employmentpost.repository;

import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPost;
import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPostWorkFieldChildTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerPostWorkFieldChildTagRepository extends JpaRepository<EmployerPostWorkFieldChildTag,Long> {

    void deleteByEmployerPost(final EmployerPost employerPost);

}
