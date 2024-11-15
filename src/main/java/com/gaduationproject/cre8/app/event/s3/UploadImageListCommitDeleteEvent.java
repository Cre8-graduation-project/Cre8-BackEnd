package com.gaduationproject.cre8.app.event.s3;

import com.gaduationproject.cre8.app.portfolio.dto.event.ImageDeleteEventDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UploadImageListCommitDeleteEvent {

    private List<ImageDeleteEventDto> imageDeleteEventDtos = new ArrayList<>();

    @Builder
    public UploadImageListCommitDeleteEvent(List<ImageDeleteEventDto> imageDeleteEventDtos){
        this.imageDeleteEventDtos = imageDeleteEventDtos;
    }

}
