package com.gaduationproject.cre8.app.event.s3;

import lombok.Builder;
import lombok.Getter;

@Getter
public class S3UploadImageRollbackEvent {

    private String newAccessImageUrl;
    @Builder
    public S3UploadImageRollbackEvent(final String newAccessImageUrl) {
        this.newAccessImageUrl = newAccessImageUrl;
    }

}
