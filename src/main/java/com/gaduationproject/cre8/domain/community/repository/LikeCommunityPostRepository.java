package com.gaduationproject.cre8.domain.community.repository;

import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
import com.gaduationproject.cre8.domain.community.entity.LikeCommunityPost;
import com.gaduationproject.cre8.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCommunityPostRepository extends JpaRepository<LikeCommunityPost,Long> {

    boolean existsByCommunityPostIdAndLikerId(final Long communityPostId,final Long memberId);

    void deleteByCommunityPostId(final Long communityPostId);
}
