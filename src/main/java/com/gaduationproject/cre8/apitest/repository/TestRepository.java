package com.gaduationproject.cre8.apitest.repository;

import com.gaduationproject.cre8.apitest.domain.ApiTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<ApiTest, Long> {

}
