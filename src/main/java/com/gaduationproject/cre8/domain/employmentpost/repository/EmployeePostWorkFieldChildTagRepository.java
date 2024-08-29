package com.gaduationproject.cre8.domain.employmentpost.repository;


import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePostWorkFieldChildTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeePostWorkFieldChildTagRepository extends JpaRepository<EmployeePostWorkFieldChildTag,Long> {

    void deleteByEmployeePost(final EmployeePost employeePost);


    @Query("select epwfct from EmployeePostWorkFieldChildTag epwfct join fetch epwfct.workFieldChildTag where epwfct.employeePost.id=:employeePostId")
    List<EmployeePostWorkFieldChildTag> findByEmployeePost_IdWithFetchWorkFieldChildTag(@Param("employeePostId") final Long employeePostId);

    List<EmployeePostWorkFieldChildTag> findByEmployeePost_Id(final Long employeePostId);
}
