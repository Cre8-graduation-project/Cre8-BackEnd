package com.gaduationproject.cre8.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileEditRequestDto {

    private String youtubeLink;
    private String twitterLink;
    private String personalLink;
    private String personalStatement;

    @Builder
    public ProfileEditRequestDto(String youtubeLink,
                                String twitterLink,
                                String personalLink,
                                 String personalStatement){
        this.youtubeLink = youtubeLink;
        this.twitterLink = twitterLink;
        this.personalLink = personalLink;
        this.personalStatement = personalStatement;
    }

}
