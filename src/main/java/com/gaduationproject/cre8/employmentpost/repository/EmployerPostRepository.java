package com.gaduationproject.cre8.employmentpost.repository;

import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPost;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployerPostRepository extends JpaRepository<EmployerPost,Long>,EmployerPostCustomRepository{

    @Query("select ep from EmployerPost ep left join fetch ep.basicPostContent.workFieldTag left join fetch ep.employerPostWorkFieldChildTagList epwfct "
            + "left join fetch epwfct.workFieldChildTag where ep.id=:employerPostId")
    Optional<EmployerPost> findByIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag(@Param("employerPostId") Long employerPostId);

}
