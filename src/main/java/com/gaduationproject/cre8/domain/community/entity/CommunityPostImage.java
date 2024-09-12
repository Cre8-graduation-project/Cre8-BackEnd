package com.gaduationproject.cre8.domain.community.entity;

import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
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
public class CommunityPostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_post_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_post_id",nullable = false)
    private CommunityPost communityPost;

    @Column(nullable = false)
    private String accessUrl;

    @Column(nullable = false)
    private String originalName;

    @Builder
    public CommunityPostImage(CommunityPost communityPost, String accessUrl,String originalName) {
        this.communityPost = communityPost;
        this.accessUrl = accessUrl;
        this.originalName = originalName;
    }
}

