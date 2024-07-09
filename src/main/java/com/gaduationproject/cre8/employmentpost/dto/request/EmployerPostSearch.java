package com.gaduationproject.cre8.employmentpost.dto.request;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class EmployerPostSearch {

    private Long workFieldId;

    private List<Long> workFieldChildTagId = new ArrayList<>();

    private Integer minCareer;

    private Integer maxCareer;

    public EmployerPostSearch(final Long workFieldId, final List<Long> workFieldChildTagId, final Integer minCareer,
            Integer maxCareer) {
        this.workFieldId = workFieldId;
        this.minCareer = minCareer;
        this.maxCareer = maxCareer;

        this.workFieldChildTagId=workFieldChildTagId==null?new ArrayList<>():workFieldChildTagId;

    }
}
