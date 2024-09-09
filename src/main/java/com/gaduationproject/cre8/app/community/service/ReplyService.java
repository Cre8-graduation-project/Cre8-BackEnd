package com.gaduationproject.cre8.app.community.service;

import com.gaduationproject.cre8.app.community.dto.request.ReplyEditRequestDto;
import com.gaduationproject.cre8.app.community.dto.request.ReplySaveRequestDto;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
import com.gaduationproject.cre8.domain.community.entity.Reply;
import com.gaduationproject.cre8.domain.community.repository.CommunityPostRepository;
import com.gaduationproject.cre8.domain.community.repository.ReplyRepository;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final CommunityPostRepository communityPostRepository;


    @Transactional
    public void saveReply(final ReplySaveRequestDto replySaveRequestDto,final String loginId){

        checkChildReply(replySaveRequestDto.getParentReplyId());

        Reply reply = Reply.builder()
                .parentReply(replySaveRequestDto.getParentReplyId()==null?null:getReplyById(replySaveRequestDto.getParentReplyId()))
                .writer(getLoginMember(loginId))
                .communityPost(getCommunityPostById(replySaveRequestDto.getCommunityPostId()))
                .contents(replySaveRequestDto.getContents())
                .build();

        replyRepository.save(reply);
    }

    @Transactional
    public void updateReply(final ReplyEditRequestDto replyEditRequestDto,final String loginId){

       Reply reply = getReplyById(replyEditRequestDto.getReplyId());

       checkAccessMember(loginId,reply);

       reply.changeReplyContents(replyEditRequestDto.getContents());
    }

    @Transactional
    public void deleteReply(final Long replyId,final String loginId){

        Reply reply = replyRepository.findById(replyId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_REPLY));

        checkAccessMember(loginId,reply);

        replyRepository.deleteByParentReply(reply);
        replyRepository.delete(reply);

    }

    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.LOGIN_ID_NOT_MATCH));
    }

    private Reply getReplyById(final Long replyId){
        return replyRepository.findById(replyId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_REPLY));
    }

    private CommunityPost getCommunityPostById(final Long communityPostId){

        return communityPostRepository.findById(communityPostId).
                orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_COMMUNITY_POST));
    }

    private void checkChildReply(final Long  parentReplyId){

        if(parentReplyId==null){
            return;
        }

        Reply parentReply = replyRepository.findById(parentReplyId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_REPLY));

        if(parentReply.getParentReply()!=null){
            throw new BadRequestException(ErrorCode.CANT_FIND_REPLY);
        }
    }

    private void checkAccessMember(final String loginId,final Reply reply){

        if(loginId==null || !loginId.equals(reply.getWriter().getLoginId())){
            throw new BadRequestException(ErrorCode.CANT_ACCESS_REPLY);
        }

    }



}
