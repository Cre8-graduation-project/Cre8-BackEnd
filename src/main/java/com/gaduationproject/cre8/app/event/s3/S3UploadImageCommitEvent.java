package com.gaduationproject.cre8.app.event.s3;

import lombok.Builder;
import lombok.Getter;

@Getter
public class S3UploadImageCommitEvent {

    private String oldAccessImageUrl;
    @Builder
    public S3UploadImageCommitEvent(final String oldAccessImageUrl) {
        this.oldAccessImageUrl = oldAccessImageUrl;
    }
}
