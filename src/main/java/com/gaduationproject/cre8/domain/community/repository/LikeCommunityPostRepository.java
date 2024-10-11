package com.gaduationproject.cre8.domain.community.repository;

import com.gaduationproject.cre8.domain.community.dto.CommunityPostSearchDBResponseDto;
import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
import com.gaduationproject.cre8.domain.community.entity.LikeCommunityPost;
import com.gaduationproject.cre8.domain.employmentpost.entity.BookMarkEmployeePost;
import com.gaduationproject.cre8.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeCommunityPostRepository extends JpaRepository<LikeCommunityPost,Long> {

    boolean existsByCommunityPostIdAndLikerId(final Long communityPostId,final Long memberId);

    void deleteByCommunityPostId(final Long communityPostId);

    Optional<LikeCommunityPost> findByLikerIdAndAndCommunityPostId(final Long memberId,final Long communityPostId);

    @Query("select lcp from LikeCommunityPost lcp join fetch lcp.communityPost join fetch lcp.liker "
            + "where lcp.liker.id=:memberId")
    Slice<LikeCommunityPost> showMyLikeCommunityPost(final Long memberId,final Pageable pageable);


    @Query("select count(*) from LikeCommunityPost lcp where lcp.communityPost.id=:communityPostId")
    int getLikeCountsByCommunityPostId(@Param("communityPostId") Long communityPostId);

    void deleteByLiker(final Member member);
}
