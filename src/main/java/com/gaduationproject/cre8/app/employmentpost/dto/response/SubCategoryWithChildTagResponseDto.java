package com.gaduationproject.cre8.app.employmentpost.dto.response;

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

    public static SubCategoryWithChildTagResponseDto of(final String subCategoryName,final List<String> childTagName){

        return new SubCategoryWithChildTagResponseDto(subCategoryName,childTagName);
    }

}
