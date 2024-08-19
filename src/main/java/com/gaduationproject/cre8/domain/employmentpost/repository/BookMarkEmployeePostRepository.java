package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.entity.BookMarkEmployeePost;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookMarkEmployeePostRepository extends JpaRepository<BookMarkEmployeePost,Long> {

    Optional<BookMarkEmployeePost> findByMemberIdAndEmployeePostId(final Long memberId,final Long employeePostId);

    void deleteByEmployeePostId(final Long employeePostId);

    boolean existsByMemberIdAndEmployeePostId(final Long memberId,final Long employeePostId);

    @Query("select bep from BookMarkEmployeePost bep join fetch bep.employeePost ep join fetch ep.basicPostContent.member "
            + "left join fetch ep.basicPostContent.workFieldTag where bep.member.id=:memberId")
    Slice<BookMarkEmployeePost> showMyBookMarkEmployeePost(final Long memberId,final Pageable pageable);
}
