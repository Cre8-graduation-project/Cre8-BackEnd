package com.cre8.aop;


import com.cre8.member.entity.Member;
import com.cre8.mongodb.domain.NotificationType;

public interface NotifyInfo {

    String getContents();

    Member receiver();

    String getSenderNickName();

    Long getPostId();

    NotificationType getNotificationType();

}
