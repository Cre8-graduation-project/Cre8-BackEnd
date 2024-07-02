package com.gaduationproject.cre8.employmentpost.domain.entity;

import com.gaduationproject.cre8.employmentpost.domain.type.EnrollDurationType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmployerPost  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employer_post_id")
    private Long id;

    @Embedded
    private BasicPostContent basicPostContent;

    private int numberOfEmployee;

    @Enumerated(EnumType.STRING)
    private EnrollDurationType enrollDurationType;

    private int minCareerYear;

    private LocalDate deadLine;



}
