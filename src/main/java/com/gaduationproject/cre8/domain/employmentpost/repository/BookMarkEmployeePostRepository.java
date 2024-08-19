package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.entity.BookMarkEmployeePost;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkEmployeePostRepository extends JpaRepository<BookMarkEmployeePost,Long> {

    Optional<BookMarkEmployeePost> findByMemberIdAndEmployeePostId(final Long memberId,final Long employeePostId);

    void deleteByEmployeePostId(final Long employeePostId);

    boolean existsByMemberIdAndEmployeePostId(final Long memberId,final Long employeePostId);

}
