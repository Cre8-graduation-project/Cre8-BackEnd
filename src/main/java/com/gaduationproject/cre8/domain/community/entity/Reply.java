package com.gaduationproject.cre8.domain.community.entity;

import com.gaduationproject.cre8.common.aop.NotifyInfo;
import com.gaduationproject.cre8.domain.baseentity.BaseEntity;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.externalApi.mongodb.domain.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reply extends BaseEntity implements NotifyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;


    @Column(length = 200)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_post_id",nullable = false,updatable = false)
    private CommunityPost communityPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_reply_id",updatable = false)
    private Reply parentReply;


    @Builder
    public Reply(final String contents, final Member writer, final CommunityPost communityPost, final Reply parentReply) {
        this.contents = contents;
        this.writer = writer;
        this.communityPost = communityPost;
        this.parentReply = parentReply;
    }

    public void changeReplyContents(final String contents){
        this.contents = contents;
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
        return id;
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.COMMUNITY;
    }
}
