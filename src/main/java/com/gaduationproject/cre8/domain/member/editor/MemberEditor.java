package com.gaduationproject.cre8.domain.member.editor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEditor {

    private String accessUrl;
    private String personalStatement;
    private String youtubeLink;
    private String personalLink;
    private String twitterLink;
    private String nickName;

    @Builder
    public MemberEditor(final String accessUrl, final String personalStatement, final String youtubeLink,
            final String personalLink, final String twitterLink,final String nickName) {
        this.accessUrl = accessUrl;
        this.personalStatement = personalStatement;
        this.youtubeLink = youtubeLink;
        this.personalLink = personalLink;
        this.twitterLink = twitterLink;
        this.nickName = nickName;
    }
}
