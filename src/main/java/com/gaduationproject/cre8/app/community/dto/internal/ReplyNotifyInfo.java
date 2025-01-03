package com.gaduationproject.cre8.app.community.dto.internal;

import com.gaduationproject.cre8.app.aop.NotifyInfo;
import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
import com.gaduationproject.cre8.domain.community.entity.Reply;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.externalApi.mongodb.domain.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
