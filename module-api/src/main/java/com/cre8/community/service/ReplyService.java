package com.cre8.community.service;


import com.cre8.community.dto.internal.ReplyNotifyInfo;
import com.cre8.community.dto.request.ReplyEditRequestDto;
import com.cre8.community.dto.request.ReplySaveRequestDto;
import com.cre8.community.entity.CommunityPost;
import com.cre8.community.entity.Reply;
import com.cre8.community.repository.CommunityPostRepository;
import com.cre8.community.repository.ReplyRepository;
import com.cre8.member.entity.Member;
import com.cre8.member.repository.MemberRepository;
import com.cre8.notify.annotation.SendNotify;
import com.cre8.response.error.ErrorCode;
import com.cre8.response.error.exception.BadRequestException;
import com.cre8.response.error.exception.NotFoundException;
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
    @SendNotify
    public ReplyNotifyInfo saveReply(final ReplySaveRequestDto replySaveRequestDto,final String loginId){

        checkChildReply(replySaveRequestDto.getParentReplyId());

        Reply reply = getReply(replySaveRequestDto, loginId);

        replyRepository.save(reply);

        return ReplyNotifyInfo.from(reply);
    }




    private Reply getReply(ReplySaveRequestDto replySaveRequestDto, String loginId) {

        Reply reply = Reply.builder()
                .parentReply(replySaveRequestDto.getParentReplyId()==null?null:getReplyById(replySaveRequestDto.getParentReplyId()))
                .writer(getLoginMember(loginId))
                .communityPost(getCommunityPostById(replySaveRequestDto.getCommunityPostId()))
                .contents(replySaveRequestDto.getContents())
                .build();

        return reply;
    }

    @Transactional
    public void updateReply(final ReplyEditRequestDto replyEditRequestDto,final String loginId){

       Reply reply = getReplyById(replyEditRequestDto.getReplyId());

       checkAccessMember(loginId,reply);

       reply.changeReplyContents(replyEditRequestDto.getContents());
    }

    @Transactional
    public void deleteReply(final Long replyId,final String loginId){

        Reply reply = replyRepository.findById(replyId).orElseThrow(()->new NotFoundException(
                ErrorCode.CANT_FIND_REPLY));

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
