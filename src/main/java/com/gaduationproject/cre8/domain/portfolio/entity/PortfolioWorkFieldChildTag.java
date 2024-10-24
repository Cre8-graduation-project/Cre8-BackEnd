package com.gaduationproject.cre8.domain.portfolio.entity;

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
public class PortfolioWorkFieldChildTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_work_field_child_tag_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_field_child_tag_id")
    private WorkFieldChildTag workFieldChildTag;

    @Builder
    public PortfolioWorkFieldChildTag(Portfolio portfolio, WorkFieldChildTag workFieldChildTag) {
        this.portfolio = portfolio;
        this.workFieldChildTag = workFieldChildTag;
    }
}
