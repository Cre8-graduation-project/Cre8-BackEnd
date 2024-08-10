package com.gaduationproject.cre8.app.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class S3UploadRollbackEvent {

    private String newAccessUrl;
    @Builder
    public S3UploadRollbackEvent(final String newAccessUrl) {
        this.newAccessUrl = newAccessUrl;
    }

}
