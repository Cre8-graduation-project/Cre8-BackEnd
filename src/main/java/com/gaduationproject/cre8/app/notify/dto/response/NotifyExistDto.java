package com.gaduationproject.cre8.app.notify.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class NotifyExistDto {

    private boolean isUnReadChat;

    private boolean isUnreadNotify;

    public static NotifyExistDto of(boolean isUnReadChat, boolean isUnreadNotify){
        return new NotifyExistDto(isUnReadChat,isUnreadNotify);
    }

}
