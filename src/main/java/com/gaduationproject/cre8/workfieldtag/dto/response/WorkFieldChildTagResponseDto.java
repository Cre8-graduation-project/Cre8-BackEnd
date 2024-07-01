package com.gaduationproject.cre8.workfieldtag.dto.response;

import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldChildTag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
