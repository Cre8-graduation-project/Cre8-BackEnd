package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeePostRepository extends JpaRepository<EmployeePost,Long>,EmployeePostCustomRepository {

    @Query("select ep from EmployeePost ep join fetch ep.basicPostContent.member left join fetch ep.basicPostContent.workFieldTag left join fetch ep.employeePostWorkFieldChildTagList epwfct "
            + "left join fetch epwfct.workFieldChildTag where ep.id=:employeePostId")
    Optional<EmployeePost> findByIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag(@Param("employeePostId") final Long employeePostId);


    @Query("select ep from EmployeePost ep join fetch ep.basicPostContent.member left join fetch ep.basicPostContent.workFieldTag "
            + "where ep.basicPostContent.title like %:keyword%")
    Slice<EmployeePost> findEmployeePostWithFetchMemberAndWorkFieldTagAndChildTagListWithSlice(@Param("keyword")final String keyword,final Pageable pageable);

    @Query("select ep from EmployeePost ep join fetch ep.basicPostContent.member m left join fetch ep.basicPostContent.workFieldTag "
            + "where m.id=:memberId")
    Slice<EmployeePost> findEmployeePostByMemberId(final Long memberId,final Pageable pageable);

}
