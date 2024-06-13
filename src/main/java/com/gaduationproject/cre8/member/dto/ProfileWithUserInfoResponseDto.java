package com.gaduationproject.cre8.member.dto;

import com.gaduationproject.cre8.member.entity.Profile;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileWithUserInfoResponseDto {
    private String userNickName;
    private String accessUrl;
    private String youtubeLink;
    private String twitterLink;
    private String personalLink;
    private String personalStatement;


    @Builder
    public ProfileWithUserInfoResponseDto(Profile profile,String userNickName,String accessUrl){
        this.userNickName = userNickName;
        this.accessUrl = accessUrl;
        this.youtubeLink = profile.getYoutubeLink();
        this.twitterLink = profile.getTwitterLink();
        this.personalLink = profile.getPersonalLink();
        this.personalStatement = profile.getPersonalStatement();
    }


}
