package com.cre8.community.service;


import com.cre8.community.dto.CommunityPostSearchDBResponseDto;
import com.cre8.community.dto.response.CommunityPostSearchResponseDto;
import com.cre8.community.dto.response.CommunityPostSearchWithSliceResponseDto;
import com.cre8.community.entity.CommunityPost;
import com.cre8.community.repository.CommunityPostRepository;
import com.cre8.community.repository.LikeCommunityPostRepository;
import com.cre8.community.repository.ReplyRepository;
import com.cre8.member.entity.Member;
import com.cre8.member.repository.MemberRepository;
import com.cre8.response.error.ErrorCode;
import com.cre8.response.error.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

    public CommunityPostSearchWithSliceResponseDto searchMyCommunityPost(final String loginId, final Pageable pageable){

        Member member = getLoginMember(loginId);

        Slice<CommunityPostSearchDBResponseDto> myCommunityPost = communityPostRepository.findMyCommunityPost(
                member.getId(), pageable);

        return CommunityPostSearchWithSliceResponseDto.of(myCommunityPost.getContent().stream().map(communityPostSearchDBResponseDto -> {

            return CommunityPostSearchResponseDto.of(communityPostSearchDBResponseDto.getCommunityPostId(),
                    communityPostSearchDBResponseDto.getTitle(),
                    replyRepository.totalReplyCount(communityPostSearchDBResponseDto.getCommunityPostId()),
                    communityPostSearchDBResponseDto.getWriterNickName(),
                    communityPostSearchDBResponseDto.getCreatedAt());
        }).toList(),myCommunityPost.hasNext());
    }

    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.CANT_FIND_MEMBER));
    }


}
