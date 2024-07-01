package com.gaduationproject.cre8.workfieldtag.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkFieldChildTagWithSubCategoryNameResponseDto {

    private String subCategoryName;

    List<WorkFieldChildTagResponseDto> workFieldChildTagResponseDtoList = new ArrayList<>();

    public static WorkFieldChildTagWithSubCategoryNameResponseDto of(String subCategoryName,List<WorkFieldChildTagResponseDto> workFieldChildTagResponseDtoList){
        return new WorkFieldChildTagWithSubCategoryNameResponseDto(subCategoryName,workFieldChildTagResponseDtoList);
    }

}
