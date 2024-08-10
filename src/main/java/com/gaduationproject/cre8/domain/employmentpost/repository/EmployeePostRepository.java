package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeePostRepository extends JpaRepository<EmployeePost,Long>,EmployeePostCustomRepository {

    @Query("select ep from EmployeePost ep join fetch ep.basicPostContent.member left join fetch ep.basicPostContent.workFieldTag left join fetch ep.employeePostWorkFieldChildTagList epwfct "
            + "left join fetch epwfct.workFieldChildTag where ep.id=:employeePostId")
    Optional<EmployeePost> findByIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag(@Param("employeePostId") final Long employeePostId);

}