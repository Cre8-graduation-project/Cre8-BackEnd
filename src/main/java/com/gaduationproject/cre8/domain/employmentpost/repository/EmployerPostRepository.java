package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeePostKeyWordSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployerPostKeyWordSearchDBResponseDto;
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

public interface EmployerPostRepository extends JpaRepository<EmployerPost,Long>,EmployerPostCustomRepository{

    @Query("select ep from EmployerPost ep left join fetch ep.basicPostContent.workFieldTag left join fetch ep.employerPostWorkFieldChildTagList epwfct "
            + "left join fetch epwfct.workFieldChildTag where ep.id=:employerPostId")
    Optional<EmployerPost> findByIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag(@Param("employerPostId") final Long employerPostId);


    @Query("select ep from EmployerPost ep join fetch ep.basicPostContent.member left join fetch ep.basicPostContent.workFieldTag"
            + " where ep.basicPostContent.title like %:keyword% or ep.companyName like %:keyword%")
    Slice<EmployerPost> findEmployerPostWithFetchMemberAndWorkFieldTagAndChildTagListWithSlice(@Param("keyword")final String keyword,final Pageable pageable);


    @Query("select ep.id from EmployerPost ep where ep.basicPostContent.title like %:keyword%")
    Page<Long> findEmployerPostIdWithPage(@Param("keyword")final String keyword,final Pageable pageable);

    @Query("select new com.gaduationproject.cre8.domain.employmentpost.dto.EmployerPostKeyWordSearchDBResponseDto(ep.id,ep.basicPostContent.title,"
            + "ep.companyName,"
            + "ep.enrollDurationType,"
            + "ep.basicPostContent.accessUrl,"
            + "ep.basicPostContent.workFieldTag) from EmployerPost ep "
            + "left outer join ep.basicPostContent.workFieldTag w "
            + "where ep.id in :employerPostIdList")
    List<EmployerPostKeyWordSearchDBResponseDto> findEmployerPostKeyWordSearchDB(@Param("employerPostIdList") List<Long> employerPostIdList,
            Sort sort);

    @Query("select ep from EmployerPost ep join fetch ep.basicPostContent.member m left join fetch ep.basicPostContent.workFieldTag "
            + "where m.id=:memberId")
    Slice<EmployerPost> findEmployerPostByMemberId(final Long memberId,final Pageable pageable);


    List<EmployerPost> findByBasicPostContent_Member(final Member member);
    void deleteByBasicPostContent_Member(final Member member);

}
