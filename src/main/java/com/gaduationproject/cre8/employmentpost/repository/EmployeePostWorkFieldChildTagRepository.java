package com.gaduationproject.cre8.employmentpost.repository;


import com.gaduationproject.cre8.employmentpost.domain.entity.EmployeePost;
import com.gaduationproject.cre8.employmentpost.domain.entity.EmployeePostWorkFieldChildTag;
import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeePostWorkFieldChildTagRepository extends JpaRepository<EmployeePostWorkFieldChildTag,Long> {

    void deleteByEmployeePost(EmployeePost employeePost);
}
