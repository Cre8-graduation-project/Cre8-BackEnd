package com.gaduationproject.cre8.app.employmentpost.dto.response;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.app.portfolio.dto.response.PortfolioSimpleResponseDto;
import com.gaduationproject.cre8.domain.member.entity.Member;
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

    private Long employeePostId;
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
    private String contact;
    private Long writerId;
    private String writerNickName;
    private String writerAccessUrl;
    private boolean isBookMarked;


    public static EmployeePostResponseDto of(final List<SubCategoryWithChildTagResponseDto> subCategoryWithChildTagResponseDtoList,
                                             final EmployeePost employeePost,
                                             final List<PortfolioSimpleResponseDto> portfolioSimpleResponseDtoList,
                                             final boolean isBookMarked){


        String workFieldName = employeePost.getBasicPostContent().getWorkFieldTag()==null?null:employeePost.getBasicPostContent().getWorkFieldTag().getName();
        Member writer = employeePost.getBasicPostContent().getMember();


        return new EmployeePostResponseDto(employeePost.getId(),
                employeePost.getBasicPostContent().getTitle(),
                writer.getName(),
                writer.getSex().getName(),
                writer.getBirthDay().getYear(),
                TagPostResponseDto.of(workFieldName,subCategoryWithChildTagResponseDtoList),
                portfolioSimpleResponseDtoList,
                employeePost.getBasicPostContent().getPayment().getPaymentMethod().getName(),
                employeePost.getBasicPostContent().getPayment().getPaymentAmount(),
                employeePost.getCareerYear(),
                employeePost.getBasicPostContent().getContents(),
                employeePost.getBasicPostContent().getContact(),
                writer.getId(),
                writer.getNickName(),
                writer.getAccessUrl(),
                isBookMarked);

    }


}
