package com.gaduationproject.cre8.employmentpost.dto.request;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class EmployerPostSearch {

    private Long workFieldId;

    private List<Long> workFieldChildTagId = new ArrayList<>();

    private int pay;

    private int minCareer;



}
