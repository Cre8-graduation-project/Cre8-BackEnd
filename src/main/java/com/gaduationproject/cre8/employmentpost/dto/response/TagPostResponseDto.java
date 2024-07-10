package com.gaduationproject.cre8.employmentpost.dto.response;

import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldChildTag;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TagPostResponseDto {

    private String workFieldTagName;
    private List<SubCategoryWithChildTagResponseDto> subCategoryWithChildTagResponseDtoList;

    public static TagPostResponseDto of(String workFieldTagName,List<SubCategoryWithChildTagResponseDto> subCategoryWithChildTagResponseDtoList){

        return new TagPostResponseDto(workFieldTagName,subCategoryWithChildTagResponseDtoList);
    }

}
