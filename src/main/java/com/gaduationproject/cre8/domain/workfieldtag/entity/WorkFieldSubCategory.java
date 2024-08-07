package com.gaduationproject.cre8.domain.workfieldtag.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkFieldSubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_field_sub_category_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_field_tag_id",nullable = false)
    private WorkFieldTag workFieldTag;


    @Builder
    public WorkFieldSubCategory(String name,WorkFieldTag workFieldTag){
        this.name = name;
        this.workFieldTag = workFieldTag;
    }

    @OneToMany(mappedBy = "workFieldSubCategory",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<WorkFieldChildTag> workFieldChildTagList = new ArrayList<>();

    public void addWorkFieldChildTag(final String workFieldChildTagName){

        WorkFieldChildTag workFieldChildTag = WorkFieldChildTag.builder()
                .workFieldSubCategory(this)
                .name(workFieldChildTagName)
                .build();

        workFieldChildTagList.add(workFieldChildTag);

    }

}
