package com.gaduationproject.cre8.app.portfolio.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class S3UploadPortfolioImageCommitEvent {

    private List<String> deleteAccessUrlList = new ArrayList<>();

    @Builder
    public S3UploadPortfolioImageCommitEvent(List<String> deleteAccessUrlList){
        this.deleteAccessUrlList = deleteAccessUrlList;
    }

}
