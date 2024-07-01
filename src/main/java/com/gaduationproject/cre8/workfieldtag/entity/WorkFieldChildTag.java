package com.gaduationproject.cre8.workfieldtag.entity;

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
public class WorkFieldChildTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_field_child_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_field_sub_category_id",nullable = false)
    private WorkFieldSubCategory workFieldSubCategory;


    @Column(nullable = false)
    private String name;

    @Builder
    public WorkFieldChildTag(WorkFieldSubCategory workFieldSubCategory,String name){
       this.workFieldSubCategory = workFieldSubCategory;
       this.name = name;
    }







}
