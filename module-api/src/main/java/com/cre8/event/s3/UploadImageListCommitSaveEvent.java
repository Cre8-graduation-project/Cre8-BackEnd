package com.cre8.event.s3;

import com.cre8.portfolio.dto.event.ImageSaveEventDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UploadImageListCommitSaveEvent {

    private List<ImageSaveEventDto> imageSaveEventDtos = new ArrayList<>();

    @Builder
    public UploadImageListCommitSaveEvent(List<ImageSaveEventDto> imageSaveEventDtos){
        this.imageSaveEventDtos = imageSaveEventDtos;
    }

}
