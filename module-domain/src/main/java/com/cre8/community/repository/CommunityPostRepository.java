package com.cre8.community.repository;


import com.cre8.community.dto.CommunityPostSearchDBResponseDto;
import com.cre8.community.entity.CommunityPost;
import com.cre8.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityPostRepository extends JpaRepository<CommunityPost,Long>,CommunityPostCustomRepository {


    @Query("select cp from CommunityPost cp join fetch cp.writer where cp.id=:communityPostId")
    Optional<CommunityPost> findCommunityPostByIdWithFetchWriter(@Param("communityPostId") Long communityPostId);

    @Query("select new com.cre8.community.dto.CommunityPostSearchDBResponseDto(cp.id,cp.title,m.nickName,cp.createdAt)"
            + "from CommunityPost cp join cp.writer m where cp.communityBoard.id=:communityBoardId")
    Slice<CommunityPostSearchDBResponseDto> findCommunityPostKeyWordSearchDBByCommunityBoardId(@Param("communityBoardId") final Long communityBoardId,
                                                                                               final Pageable pageable);

    void deleteByWriter(final Member member);

    List<CommunityPost> findByWriter(final Member member);


    @Query("select new com.cre8.community.dto.CommunityPostSearchDBResponseDto(cp.id,cp.title,m.nickName,cp.createdAt)"
            + "from CommunityPost cp join cp.writer m where cp.writer.id=:memberId")
    Slice<CommunityPostSearchDBResponseDto> findMyCommunityPost(@Param("memberId") final Long memberId,
            final Pageable pageable);


}
