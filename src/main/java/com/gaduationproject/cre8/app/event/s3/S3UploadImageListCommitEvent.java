package com.gaduationproject.cre8.app.event.s3;

import com.gaduationproject.cre8.app.portfolio.dto.event.ImageDeleteEventDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class S3UploadImageListCommitEvent {

    private List<ImageDeleteEventDto> imageDeleteEventDtos = new ArrayList<>();

    @Builder
    public S3UploadImageListCommitEvent(List<ImageDeleteEventDto> imageDeleteEventDtos){
        this.imageDeleteEventDtos = imageDeleteEventDtos;
    }

}
