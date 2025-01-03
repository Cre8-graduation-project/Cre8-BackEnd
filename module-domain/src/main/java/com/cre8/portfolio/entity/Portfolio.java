package com.cre8.portfolio.entity;


import com.cre8.member.entity.Member;
import com.cre8.workfieldtag.entity.WorkFieldTag;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false )
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_field_tag_id")
    private WorkFieldTag workFieldTag;


    @OneToMany(mappedBy = "portfolio",cascade = CascadeType.ALL,orphanRemoval = true)
    List<PortfolioImage> portfolioImageList = new ArrayList<>();


    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder
    public Portfolio(Member member) {
        this.member = member;
        this.workFieldTag = null;
        this.description = null;
    }

    public void changeWorkFieldTagAndDescription(WorkFieldTag workFieldTag,String description){
        this.workFieldTag = workFieldTag;
        this.description = description;
    }


}
