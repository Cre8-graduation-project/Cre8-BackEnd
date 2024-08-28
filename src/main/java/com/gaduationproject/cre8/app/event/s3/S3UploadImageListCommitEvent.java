package com.gaduationproject.cre8.app.event.s3;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class S3UploadImageListCommitEvent {

    private List<String> deleteAccessImageUrlList = new ArrayList<>();

    @Builder
    public S3UploadImageListCommitEvent(List<String> deleteAccessImageUrlList){
        this.deleteAccessImageUrlList = deleteAccessImageUrlList;
    }

}
