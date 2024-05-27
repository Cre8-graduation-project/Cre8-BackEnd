package com.gaduationproject.cre8.employmentpost.entity;

import com.gaduationproject.cre8.employmentpost.type.EnrollDurationType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("employer_post")
public class EmployerPost extends EmploymentPost{

    private int numberOfEmployee;

    @Enumerated(EnumType.STRING)
    private EnrollDurationType enrollDurationType;

    private int minCareerYear;



}
