package com.cre8.community.dto.internal;


import com.cre8.aop.NotifyInfo;
import com.cre8.community.entity.CommunityPost;
import com.cre8.community.entity.Reply;
import com.cre8.member.entity.Member;
import com.cre8.mongodb.domain.NotificationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyNotifyInfo implements NotifyInfo {


    private String contents;

    private Member writer;

    private CommunityPost communityPost;

    private Reply parentReply;

    public static ReplyNotifyInfo from(Reply reply) {
        return new ReplyNotifyInfo(reply.getContents(),reply.getWriter(),reply.getCommunityPost(),reply.getParentReply());
    }


    @Override
    public Member receiver() {
        return communityPost.getWriter();
    }

    @Override
    public String getSenderNickName() {
        return writer.getNickName();
    }

    @Override
    public Long getPostId(){
        return communityPost.getId();
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.COMMUNITY;
    }

}
