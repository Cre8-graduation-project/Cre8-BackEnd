package com.gaduationproject.cre8.app.aop;

import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.externalApi.mongodb.domain.NotificationType;

public interface NotifyInfo {

    String getContents();

    Member receiver();

    String getSenderNickName();

    Long getPostId();

    NotificationType getNotificationType();

}
