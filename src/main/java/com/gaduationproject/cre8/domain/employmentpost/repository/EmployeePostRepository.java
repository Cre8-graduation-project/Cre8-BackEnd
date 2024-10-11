package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeePostKeyWordSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
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

    @Query("select ep.id from EmployeePost ep where ep.basicPostContent.title like %:keyword%")
    Page<Long> findEmployeePostIdWithPage(@Param("keyword")final String keyword,final Pageable pageable);

    @Query("select new com.gaduationproject.cre8.domain.employmentpost.dto.EmployeePostKeyWordSearchDBResponseDto(ep.id,ep.basicPostContent.title,"
            + "w,m.name,ep.basicPostContent.accessUrl,"
            + "m.sex,"
            + "m.birthDay) from EmployeePost ep join ep.basicPostContent.member m "
            + "left outer join ep.basicPostContent.workFieldTag w "
            + "where ep.id in :employeePostIdList")
    List<EmployeePostKeyWordSearchDBResponseDto> findEmployeePostKeyWordSearchDB(@Param("employeePostIdList") List<Long> employeePostIdList,
            Sort sort);

    @Query("select ep from EmployeePost ep join fetch ep.basicPostContent.member m left join fetch ep.basicPostContent.workFieldTag "
            + "where m.id=:memberId")
    Slice<EmployeePost> findEmployeePostByMemberId(final Long memberId,final Pageable pageable);


    List<EmployeePost> findByBasicPostContent_Member(final Member member);
    void deleteByBasicPostContent_Member(final Member member);

}
