package com.gaduationproject.cre8.domain.employmentpost.entity;

import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
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
public class EmployerPostWorkFieldChildTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employer_post_work_field_child_tag")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_post_id")
    private EmployerPost employerPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_field_child_tag_id")
    private WorkFieldChildTag workFieldChildTag;

    @Builder
    public EmployerPostWorkFieldChildTag(final EmployerPost employerPost,
            final WorkFieldChildTag workFieldChildTag) {

        this.employerPost = employerPost;
        this.workFieldChildTag = workFieldChildTag;
        employerPost.getEmployerPostWorkFieldChildTagList().add(this);
    }
}
