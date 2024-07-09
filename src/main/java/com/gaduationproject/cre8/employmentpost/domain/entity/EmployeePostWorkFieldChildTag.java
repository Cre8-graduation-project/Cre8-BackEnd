package com.gaduationproject.cre8.employmentpost.domain.entity;

import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldChildTag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmployeePostWorkFieldChildTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_post_work_field_child_tag")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_post_id")
    private EmployeePost employeePost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_field_child_tag_id")
    private WorkFieldChildTag workFieldChildTag;



    @Builder
    public EmployeePostWorkFieldChildTag(EmployeePost employeePost,
            WorkFieldChildTag workFieldChildTag) {
        this.employeePost = employeePost;
        this.workFieldChildTag = workFieldChildTag;
        employeePost.getEmployeePostWorkFieldChildTagList().add(this);
    }

}
