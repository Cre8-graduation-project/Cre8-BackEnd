package com.gaduationproject.cre8.domain.community.repository;

import com.gaduationproject.cre8.domain.community.dto.CommunityPostSearchDBResponseDto;
import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployerPostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CommunityPostCustomRepository {

    Slice<CommunityPostSearchDBResponseDto> showCommunityPostWithNoOffSet(final Long lastCommunityPostId,final Long communityBoardId,final Pageable pageable);

}
