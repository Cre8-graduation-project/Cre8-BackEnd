package com.cre8.community.repository;


import com.cre8.community.dto.CommunityPostSearchDBResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CommunityPostCustomRepository {

    Slice<CommunityPostSearchDBResponseDto> showCommunityPostWithNoOffSet(final Long lastCommunityPostId,final Long communityBoardId,final Pageable pageable);

}
