package com.gaduationproject.cre8.domain.community.repository;

import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
import com.gaduationproject.cre8.domain.community.entity.CommunityPostImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPostImageRepository extends JpaRepository<CommunityPostImage,Long> {

    List<CommunityPostImage> findByCommunityPost(CommunityPost communityPost);

    void deleteByCommunityPost(CommunityPost communityPost);

}
