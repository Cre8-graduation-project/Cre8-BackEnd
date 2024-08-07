package com.gaduationproject.cre8.api.member.dto;

import com.gaduationproject.cre8.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileWithUserInfoResponseDto {
    private String userNickName;
    private String accessUrl;
    private String youtubeLink;
    private String twitterLink;
    private String personalLink;
    private String personalStatement;


    public static ProfileWithUserInfoResponseDto of(Member member){
        return new ProfileWithUserInfoResponseDto(member.getNickName(),member.getAccessUrl(),member.getYoutubeLink(),
                member.getTwitterLink(),member.getPersonalLink(),member.getPersonalStatement());
    }


}
