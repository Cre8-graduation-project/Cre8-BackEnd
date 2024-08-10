package com.gaduationproject.cre8.domain.apitest.repository;

import com.gaduationproject.cre8.domain.apitest.entity.ApiTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<ApiTest, Long> {

}
