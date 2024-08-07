package com.gaduationproject.cre8.api.employmentpost.dto.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class EmployeePostSearch {

    private Long workFieldId;
    private List<Long> workFieldChildTagId = new ArrayList<>();
    private Integer minCareer;
    private Integer maxCareer;

    public EmployeePostSearch(final Long workFieldId, final List<Long> workFieldChildTagId, final Integer minCareer,
            final Integer maxCareer) {

        this.workFieldId = workFieldId;
        this.minCareer = minCareer;
        this.maxCareer = maxCareer;
        this.workFieldChildTagId=workFieldChildTagId==null?new ArrayList<>():workFieldChildTagId;

    }

}
