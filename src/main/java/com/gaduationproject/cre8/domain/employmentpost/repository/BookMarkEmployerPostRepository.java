package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.entity.BookMarkEmployerPost;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkEmployerPostRepository extends JpaRepository<BookMarkEmployerPost,Long> {

    Optional<BookMarkEmployerPost> findByMemberIdAndEmployerPostId(final Long memberId,final Long employerPostId);

    void deleteByEmployerPostId(final Long employerPostId);

    boolean existsByMemberIdAndEmployerPostId(final Long memberId,final Long employerPostId);

}
