package com.gaduationproject.cre8.workfieldtag.repository;

import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkFieldTagRepository extends JpaRepository<WorkFieldTag,Long> {

    boolean existsByName(String name);

}
