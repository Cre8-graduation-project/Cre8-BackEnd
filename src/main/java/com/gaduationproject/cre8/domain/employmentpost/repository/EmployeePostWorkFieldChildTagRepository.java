package com.gaduationproject.cre8.domain.employmentpost.repository;


import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePostWorkFieldChildTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeePostWorkFieldChildTagRepository extends JpaRepository<EmployeePostWorkFieldChildTag,Long> {

    void deleteByEmployeePost(final EmployeePost employeePost);
}
