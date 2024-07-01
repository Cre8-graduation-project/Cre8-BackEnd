package com.gaduationproject.cre8.workfieldtag.dto.response;

import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldSubCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkFieldSubCategoryResponseDto {

    private Long workFieldSubCategoryId;

    private String workFieldSubCategoryName;

    public static WorkFieldSubCategoryResponseDto from(WorkFieldSubCategory workFieldSubCategory){
        return new WorkFieldSubCategoryResponseDto(workFieldSubCategory.getId(),
                workFieldSubCategory.getName());
    }

}
