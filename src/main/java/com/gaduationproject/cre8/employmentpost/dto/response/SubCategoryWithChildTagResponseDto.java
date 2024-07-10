package com.gaduationproject.cre8.employmentpost.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SubCategoryWithChildTagResponseDto {

    private String subCategoryName;
    private List<String> childTagName;

    public static SubCategoryWithChildTagResponseDto of(String subCategoryName,List<String> childTagName){
        return new SubCategoryWithChildTagResponseDto(subCategoryName,childTagName);
    }

}
