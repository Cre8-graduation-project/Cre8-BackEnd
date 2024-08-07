package com.gaduationproject.cre8.api.workfieldtag.dto.response;

import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkFieldTagResponseDto {

    private String name;

    private Long workFieldTagId;

    public static WorkFieldTagResponseDto of(WorkFieldTag workFieldTag){
        return new WorkFieldTagResponseDto(workFieldTag.getName(), workFieldTag.getId());
    }

}
