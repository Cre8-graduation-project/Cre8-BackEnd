package com.gaduationproject.cre8.member.dto;

import com.gaduationproject.cre8.member.entity.Profile;
import com.gaduationproject.cre8.member.repository.ProfileRepository;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private String youtubeLink;
    private String twitterLink;
    private String personalLink;
    private String personalStatement;


    @Builder
    public ProfileResponseDto(Profile profile){
        this.youtubeLink = profile.getYoutubeLink();
        this.twitterLink = profile.getTwitterLink();
        this.personalLink = profile.getPersonalLink();
        this.personalStatement = profile.getPersonalStatement();
    }


}
