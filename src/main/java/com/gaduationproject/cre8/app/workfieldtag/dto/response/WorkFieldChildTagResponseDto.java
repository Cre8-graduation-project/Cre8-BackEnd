package com.gaduationproject.cre8.app.workfieldtag.dto.response;

import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkFieldChildTagResponseDto {

    private Long workFieldChildTagId;

    private String name;

   public static WorkFieldChildTagResponseDto from(WorkFieldChildTag workFieldChildTag){
       return new WorkFieldChildTagResponseDto(workFieldChildTag.getId(),
               workFieldChildTag.getName());
   }
}
