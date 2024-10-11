package com.gaduationproject.cre8.domain.employmentpost.repository;

import com.gaduationproject.cre8.domain.employmentpost.entity.BookMarkEmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.BookMarkEmployerPost;
import com.gaduationproject.cre8.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookMarkEmployerPostRepository extends JpaRepository<BookMarkEmployerPost,Long> {

    Optional<BookMarkEmployerPost> findByMemberIdAndEmployerPostId(final Long memberId,final Long employerPostId);

    void deleteByEmployerPostId(final Long employerPostId);

    boolean existsByMemberIdAndEmployerPostId(final Long memberId,final Long employerPostId);

    @Query("select bep from BookMarkEmployerPost bep join fetch bep.employerPost ep join fetch ep.basicPostContent.member "
            + "left join fetch ep.basicPostContent.workFieldTag where bep.member.id=:memberId")
    Slice<BookMarkEmployerPost> showMyBookMarkEmployerPost(final Long memberId,final Pageable pageable);

    void deleteByMember(final Member member);

}
