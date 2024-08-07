package com.gaduationproject.cre8.domain.workfieldtag.repository;

import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldSubCategory;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkFieldSubCategoryRepository extends JpaRepository<WorkFieldSubCategory,Long> {

    boolean existsByNameAndWorkFieldTag(String name, WorkFieldTag workFieldTag);



    @Query("select wfct from WorkFieldSubCategory wfct join fetch wfct.workFieldChildTagList where wfct.workFieldTag.id =:workFieldTagId")
    List<WorkFieldSubCategory> findByWorkFieldTagWithFetchWorkFieldChildTagList(@Param("workFieldTagId") Long workFieldTagId);


    List<WorkFieldSubCategory> findByWorkFieldTagId(Long workFieldTagId);

}
