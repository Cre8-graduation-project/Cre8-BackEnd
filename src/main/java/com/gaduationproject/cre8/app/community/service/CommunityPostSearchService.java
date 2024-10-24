package com.gaduationproject.cre8.app.community.service;

import com.gaduationproject.cre8.app.community.dto.response.CommunityPostSearchResponseDto;
import com.gaduationproject.cre8.app.community.dto.response.CommunityPostSearchWithSliceResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchWithCountResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchWithSliceResponseDto;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.community.dto.CommunityPostSearchDBResponseDto;
import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
import com.gaduationproject.cre8.domain.community.repository.CommunityPostRepository;
import com.gaduationproject.cre8.domain.community.repository.LikeCommunityPostRepository;
import com.gaduationproject.cre8.domain.community.repository.ReplyRepository;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeePostKeyWordSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommunityPostSearchService {


    private final CommunityPostRepository communityPostRepository;
    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final LikeCommunityPostRepository likeCommunityPostRepository;


//    public CommunityPostSearchWithSliceResponseDto searchCommunityPostByCommunityBoardId(final Long communityBoardId,
//                                                                                         final Pageable pageable){
//
//        Slice<CommunityPostSearchDBResponseDto> communityPosts =
//                communityPostRepository.findCommunityPostKeyWordSearchDBByCommunityBoardId(communityBoardId,pageable);
//
//
//        List<CommunityPostSearchResponseDto> communityPostSearchResponseDtoList =
//                communityPosts.stream().map(communityPostSearchDBResponseDto -> {
//                    return  CommunityPostSearchResponseDto.of(communityPostSearchDBResponseDto.getCommunityPostId(),
//                            communityPostSearchDBResponseDto.getTitle(),
//                            replyRepository.totalReplyCount(communityPostSearchDBResponseDto.getCommunityPostId()),
//                            communityPostSearchDBResponseDto.getWriterNickName(),
//                            communityPostSearchDBResponseDto.getCreatedAt());
//                }).collect(Collectors.toList());
//
//        return CommunityPostSearchWithSliceResponseDto.of(communityPostSearchResponseDtoList,communityPosts.hasNext());
//
//    }

    public CommunityPostSearchWithSliceResponseDto searchCommunityPostByCommunityBoardIdAndLastPostId(final Long communityBoardId,
            final Long lastPostId, final Pageable pageable){

        Slice<CommunityPostSearchDBResponseDto> communityPosts =
                communityPostRepository.showCommunityPostWithNoOffSet(lastPostId,communityBoardId,pageable);


        List<CommunityPostSearchResponseDto> communityPostSearchResponseDtoList =
                communityPosts.stream().map(communityPostSearchDBResponseDto -> {
                    return  CommunityPostSearchResponseDto.of(communityPostSearchDBResponseDto.getCommunityPostId(),
                            communityPostSearchDBResponseDto.getTitle(),
                            replyRepository.totalReplyCount(communityPostSearchDBResponseDto.getCommunityPostId()),
                            communityPostSearchDBResponseDto.getWriterNickName(),
                            communityPostSearchDBResponseDto.getCreatedAt());
                }).collect(Collectors.toList());

        return CommunityPostSearchWithSliceResponseDto.of(communityPostSearchResponseDtoList,communityPosts.hasNext());

    }



    public CommunityPostSearchWithSliceResponseDto searchMyLikeCommunityPost(final String loginId,final Pageable pageable){

        Member member = getLoginMember(loginId);

        Slice<CommunityPost> likeCommunityPostSlice =
                likeCommunityPostRepository.showMyLikeCommunityPost(member.getId(),pageable).map(likeCommunityPost -> likeCommunityPost.getCommunityPost());

        return CommunityPostSearchWithSliceResponseDto.of(likeCommunityPostSlice.getContent().stream().map(communityPost -> {

            return  CommunityPostSearchResponseDto.of(communityPost.getId(),
                    communityPost.getTitle(),
                    replyRepository.totalReplyCount(communityPost.getId()),
                    communityPost.getWriter().getNickName(),
                    communityPost.getCreatedAt());
        }).collect(Collectors.toList()), likeCommunityPostSlice.hasNext());

    }

    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.CANT_FIND_MEMBER));
    }


}
