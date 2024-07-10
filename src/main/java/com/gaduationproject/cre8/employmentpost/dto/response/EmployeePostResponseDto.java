package com.gaduationproject.cre8.employmentpost.dto.response;

import com.gaduationproject.cre8.employmentpost.domain.entity.EmployeePost;
import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPost;
import com.gaduationproject.cre8.member.type.Sex;
import com.gaduationproject.cre8.portfolio.dto.response.PortfolioSimpleResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeePostResponseDto {

    private String title;
    private String name;
    private String sex;
    private int birthYear;
    private TagPostResponseDto tagPostResponseDto;
    private List<PortfolioSimpleResponseDto> portfolioSimpleResponseDtoList = new ArrayList<>();
    private String paymentMethod;
    private Integer paymentAmount;
    private Integer careerYear;
    private String contents;


    public static EmployeePostResponseDto of(final List<SubCategoryWithChildTagResponseDto> subCategoryWithChildTagResponseDtoList,
                                             final EmployeePost employeePost,
                                             final List<PortfolioSimpleResponseDto> portfolioSimpleResponseDtoList){

        String workFieldName = employeePost.getBasicPostContent().getWorkFieldTag()==null?null:employeePost.getBasicPostContent().getWorkFieldTag().getName();

        return new EmployeePostResponseDto(employeePost.getBasicPostContent().getTitle(),
                employeePost.getBasicPostContent().getMember().getName(),
                employeePost.getBasicPostContent().getMember().getSex().getName(),
                employeePost.getBasicPostContent().getMember().getBirthDay().getYear(),
                TagPostResponseDto.of(workFieldName,subCategoryWithChildTagResponseDtoList),
                portfolioSimpleResponseDtoList,
                employeePost.getBasicPostContent().getPayment().getPaymentMethod().getName(),
                employeePost.getBasicPostContent().getPayment().getPaymentAmount(),
                employeePost.getCareerYear(),
                employeePost.getBasicPostContent().getContents());

    }


}
