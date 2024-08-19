package com.gaduationproject.cre8.domain.employmentpost.entity;

import com.gaduationproject.cre8.domain.baseentity.BaseEntity;
import com.gaduationproject.cre8.domain.member.entity.Member;
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
public class BookMarkEmployerPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_mark_employer_post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_post_id",updatable = false)
    private EmployerPost employerPost;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",updatable = false)
    private Member member;

    @Builder
    public BookMarkEmployerPost(EmployerPost employerPost, Member member) {
        this.employerPost = employerPost;
        this.member = member;
    }
}
