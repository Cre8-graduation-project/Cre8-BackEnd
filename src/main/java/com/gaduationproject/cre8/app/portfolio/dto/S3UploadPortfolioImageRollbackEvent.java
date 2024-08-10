package com.gaduationproject.cre8.app.portfolio.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class S3UploadPortfolioImageRollbackEvent {

    private List<String> newAccessUrlList = new ArrayList<>();

    @Builder
    public S3UploadPortfolioImageRollbackEvent(List<String> newAccessUrlList){
        this.newAccessUrlList = newAccessUrlList;
    }

}

