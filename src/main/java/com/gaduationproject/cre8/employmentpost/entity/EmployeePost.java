package com.gaduationproject.cre8.employmentpost.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EmployeePost extends EmploymentPost{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private int careerYear;


}
