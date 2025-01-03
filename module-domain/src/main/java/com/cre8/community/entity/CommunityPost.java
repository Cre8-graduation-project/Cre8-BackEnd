package com.cre8.community.entity;


import com.cre8.baseentity.BaseEntity;
import com.cre8.member.entity.Member;
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
public class CommunityPost extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000,nullable = false)
    private String contents;

    private String accessUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_board_id",nullable = false)
    private CommunityBoard communityBoard;

    @Builder
    public CommunityPost(final String title, final String contents,final String accessUrl,
            final Member writer, final CommunityBoard communityBoard) {

        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.accessUrl = accessUrl;
        this.communityBoard = communityBoard;
    }

    public void changeTitleAndContentsAndAccessUrl(final String title,final String contents,final String accessUrl){

        this.title = title;
        this.contents = contents;
        this.accessUrl = accessUrl;
    }

    public void changeTitleAndContents(final String title,final String contents){

        this.title = title;
        this.contents = contents;

    }


}
