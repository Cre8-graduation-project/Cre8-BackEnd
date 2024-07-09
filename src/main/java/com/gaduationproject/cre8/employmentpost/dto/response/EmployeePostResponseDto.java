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
    private List<String> tagName = new ArrayList<>();
    private List<PortfolioSimpleResponseDto> portfolioSimpleResponseDtoList = new ArrayList<>();
    private String paymentMethod;
    private int paymentAmount;
    private int careerYear;


    public static EmployeePostResponseDto of(final EmployeePost employeePost,final List<String> tagName,
                                             final List<PortfolioSimpleResponseDto> portfolioSimpleResponseDtoList){

        return new EmployeePostResponseDto(employeePost.getBasicPostContent().getTitle(),
                employeePost.getBasicPostContent().getMember().getName(),
                employeePost.getBasicPostContent().getMember().getSex().getName(),
                employeePost.getBasicPostContent().getMember().getBirthDay().getYear(),
                tagName,
                portfolioSimpleResponseDtoList,
                employeePost.getBasicPostContent().getPayment().getPaymentMethod().getName(),
                employeePost.getBasicPostContent().getPayment().getPaymentAmount(),
                employeePost.getCareerYear());

    }


}
