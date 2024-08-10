package com.gaduationproject.cre8.app.employmentpost.dto.response;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployerPostResponseDto {

    private String title;
    private String companyName;
    private TagPostResponseDto tagPostResponseDto;
    private String paymentMethod;
    private Integer paymentAmount;
    private Integer numberOfEmployee;
    private String enrollDurationType;
    private LocalDate localDate;
    private Integer hopeCareerYear;
    private String contents;
    private String contact;

    public static EmployerPostResponseDto of(final List<SubCategoryWithChildTagResponseDto> subCategoryWithChildTagResponseDtoList, final EmployerPost employerPost){

        String workFieldName = employerPost.getBasicPostContent().getWorkFieldTag()==null?null:employerPost.getBasicPostContent().getWorkFieldTag().getName();

        return new EmployerPostResponseDto(employerPost.getBasicPostContent().getTitle(),employerPost.getCompanyName(),
                TagPostResponseDto.of(workFieldName,subCategoryWithChildTagResponseDtoList),
                employerPost.getBasicPostContent().getPayment().getPaymentMethod().getName(),
                employerPost.getBasicPostContent().getPayment().getPaymentAmount(),employerPost.getNumberOfEmployee(),employerPost.getEnrollDurationType().getName(),
                employerPost.getDeadLine(),employerPost.getHopeCareerYear(),
                employerPost.getBasicPostContent().getContents(),
                employerPost.getBasicPostContent().getContact()
        );

    }


}
