package com.gaduationproject.cre8.app.community.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
import com.gaduationproject.cre8.domain.community.entity.LikeCommunityPost;
import com.gaduationproject.cre8.domain.community.repository.CommunityPostRepository;
import com.gaduationproject.cre8.domain.community.repository.LikeCommunityPostRepository;
import com.gaduationproject.cre8.domain.employmentpost.entity.BookMarkEmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.repository.BookMarkEmployerPostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployerPostRepository;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
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


        //이미 눌러져 있을 때 deleteStareEmployerPost 수행, 없다면 saveStareEmployerPost 수행
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
