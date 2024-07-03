package com.gaduationproject.cre8.employmentpost.dto.response;

import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPost;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployerPostResponseDto {

    private List<String> tagName = new ArrayList<>();

    private String paymentMethod;

    private int pay;

    private int numberOfEmployee;

    private String enrollDurationType;

    private LocalDate localDate;

    private int minCareerYear;

    public static EmployerPostResponseDto from(List<String> tagName, EmployerPost employerPost){

        return new EmployerPostResponseDto(tagName,employerPost.getBasicPostContent().getPaymentMethod().name(),
                employerPost.getBasicPostContent().getPayment(),employerPost.getNumberOfEmployee(),employerPost.getEnrollDurationType().getName(),
                employerPost.getDeadLine(),employerPost.getMinCareerYear());

    }


}