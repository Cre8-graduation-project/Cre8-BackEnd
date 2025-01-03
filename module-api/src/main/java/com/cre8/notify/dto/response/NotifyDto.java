package com.cre8.notify.dto.response;


import com.cre8.mongodb.domain.NotificationType;
import com.cre8.mongodb.domain.Notify;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class NotifyDto {

    private String contents;

    private NotificationType notificationType;

    private Long postId;


    public static NotifyDto from(Notify notify){

        return new NotifyDto(notify.getContents(),notify.getNotificationType(),notify.getPostId());
    }


}
