package com.gaduationproject.cre8.domain.workfieldtag.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkFieldTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_field_tag_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Builder
    public WorkFieldTag(String name){
        this.name = name;
    }

    @OneToMany(mappedBy = "workFieldTag",cascade = CascadeType.ALL,orphanRemoval = true)
    List<WorkFieldSubCategory> workFieldSubCategoryList = new ArrayList<>();

    public void addWorkFieldSubCategory(final String subCategoryName){

        WorkFieldSubCategory workFieldSubCategory = WorkFieldSubCategory.builder()
                .workFieldTag(this)
                .name(subCategoryName)
                .build();

        workFieldSubCategoryList.add(workFieldSubCategory);

    }



}
