package com.gaduationproject.cre8.app.event.s3;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class S3UploadImageListRollbackEvent {

    private List<String> newAccessImageUrlList = new ArrayList<>();

    @Builder
    public S3UploadImageListRollbackEvent(List<String> newAccessImageUrlList){
        this.newAccessImageUrlList = newAccessImageUrlList;
    }

}

