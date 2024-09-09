package com.gaduationproject.cre8.domain.community.repository;

import com.gaduationproject.cre8.domain.community.dto.CommunityPostSearchDBResponseDto;
import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityPostRepository extends JpaRepository<CommunityPost,Long> {


    @Query("select cp from CommunityPost cp join fetch cp.writer where cp.id=:communityPostId")
    Optional<CommunityPost> findCommunityPostByIdWithFetchWriter(@Param("communityPostId") Long communityPostId);

    @Query("select new com.gaduationproject.cre8.domain.community.dto.CommunityPostSearchDBResponseDto(cp.id,cp.title,m.nickName,cp.createdAt)"
            + "from CommunityPost cp join cp.writer m where cp.communityBoard.id=:communityBoardId")
    Slice<CommunityPostSearchDBResponseDto> findCommunityPostKeyWordSearchDBByCommunityBoardId(@Param("communityBoardId") final Long communityBoardId,
                                                                                               final Pageable pageable);



}
