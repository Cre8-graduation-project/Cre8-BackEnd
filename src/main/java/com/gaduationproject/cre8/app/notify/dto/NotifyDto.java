package com.gaduationproject.cre8.app.notify.dto;

import com.gaduationproject.cre8.externalApi.mongodb.domain.Notify;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotifyDto {

    private String notifyId;
    private String content;
    private String url;
    private String type;


    public static NotifyDto from(Notify notify){
        return new NotifyDto(notify.getId(),notify.getContents(),notify.getUrl(),notify.getNotificationType());
    }

}
