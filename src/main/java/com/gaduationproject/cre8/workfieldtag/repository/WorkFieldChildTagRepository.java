package com.gaduationproject.cre8.workfieldtag.repository;

import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkFieldChildTagRepository extends JpaRepository<WorkFieldChildTag,Long> {

    boolean existsByNameAndWorkFieldSubCategory(String name, WorkFieldSubCategory workFieldSubCategory);


}
