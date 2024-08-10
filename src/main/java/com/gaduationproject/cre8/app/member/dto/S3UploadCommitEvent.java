package com.gaduationproject.cre8.app.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class S3UploadCommitEvent {

    private String oldAccessUrl;
    @Builder
    public S3UploadCommitEvent(final String oldAccessUrl) {
        this.oldAccessUrl = oldAccessUrl;
    }
}
