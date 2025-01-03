package com.cre8.community.service;


import com.cre8.community.entity.CommunityPost;
import com.cre8.community.entity.LikeCommunityPost;
import com.cre8.community.repository.CommunityPostRepository;
import com.cre8.community.repository.LikeCommunityPostRepository;
import com.cre8.member.entity.Member;
import com.cre8.member.repository.MemberRepository;
import com.cre8.response.error.ErrorCode;
import com.cre8.response.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeCommunityPostService {

    private final CommunityPostRepository communityPostRepository;
    private final LikeCommunityPostRepository likeCommunityPostRepository;
    private final MemberRepository memberRepository;



    //Post 에 대해 구독 를 누를 때를 위한 메서드
    @Transactional
    public void likeCommunityPost(final String loginId,final Long communityPostId){

        final Member currentMember = getLoginMember(loginId);
        final CommunityPost communityPost = findCommunityPostById(communityPostId);


        //이미 눌러져 있을 때 삭제, 처음이면 저장
        likeCommunityPostRepository.findByLikerIdAndAndCommunityPostId(currentMember.getId(), communityPost.getId())
                .ifPresentOrElse(likeCommunityPost -> {
                    cancelLikeCommunityPost(likeCommunityPost);
                },()->{
                    saveLikeCommunityPost(currentMember,communityPost);
                });


    }


    // 구독 누를 시 사용 메서드
    private void saveLikeCommunityPost(final Member member,final CommunityPost communityPost){

        LikeCommunityPost likeCommunityPost = LikeCommunityPost.builder()
                .communityPost(communityPost)
                .liker(member)
                .build();

        likeCommunityPostRepository.save(likeCommunityPost);
    }

    //구독 한번 더 눌러 취소 시킬 때 사용 메서드
    private void cancelLikeCommunityPost(final LikeCommunityPost likeCommunityPost){
        likeCommunityPostRepository.delete(likeCommunityPost);
    }

    private CommunityPost findCommunityPostById(final Long communityPostId){
        return communityPostRepository.findById(communityPostId).orElseThrow(()->new NotFoundException(
                ErrorCode.CANT_FIND_COMMUNITY_POST));
    }

    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.LOGIN_ID_NOT_MATCH));
    }

}
