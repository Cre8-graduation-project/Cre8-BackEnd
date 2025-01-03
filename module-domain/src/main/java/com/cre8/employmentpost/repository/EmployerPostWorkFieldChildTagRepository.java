package com.cre8.employmentpost.repository;


import com.cre8.employmentpost.entity.EmployerPost;
import com.cre8.employmentpost.entity.EmployerPostWorkFieldChildTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerPostWorkFieldChildTagRepository extends JpaRepository<EmployerPostWorkFieldChildTag,Long> {

    void deleteByEmployerPost(final EmployerPost employerPost);

    List<EmployerPostWorkFieldChildTag> findByEmployerPost_Id(final Long employerPostId);

}
