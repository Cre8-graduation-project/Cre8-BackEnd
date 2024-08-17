package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployerPostRepository extends JpaRepository<EmployerPost,Long>,EmployerPostCustomRepository{

    @Query("select ep from EmployerPost ep left join fetch ep.basicPostContent.workFieldTag left join fetch ep.employerPostWorkFieldChildTagList epwfct "
            + "left join fetch epwfct.workFieldChildTag where ep.id=:employerPostId")
    Optional<EmployerPost> findByIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag(@Param("employerPostId") final Long employerPostId);


    @Query("select ep from EmployerPost ep join fetch ep.basicPostContent.member left join fetch ep.basicPostContent.workFieldTag"
            + " where ep.basicPostContent.title like %:keyword% or ep.companyName like %:keyword%")
    Slice<EmployerPost> findEmployerPostWithFetchMemberAndWorkFieldTagAndChildTagListWithSlice(@Param("keyword")final String keyword,final Pageable pageable);
}
