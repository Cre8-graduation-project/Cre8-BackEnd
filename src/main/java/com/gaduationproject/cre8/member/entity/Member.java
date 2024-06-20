package com.gaduationproject.cre8.member.entity;

import com.gaduationproject.cre8.member.type.Sex;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(updatable = false,nullable = false)
    private String name;


    @Column(length = 50,unique = true,updatable = false,nullable = false)
    private String email;

    @Column(length = 20,unique = true,nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(length = 20,nullable = false,unique = true)
    private String nickName;

    @Column(nullable = false)
    private LocalDate birthDay;

    @Column(nullable = false)
    private String accessUrl;

    @Enumerated(EnumType.STRING)
    private Sex sex;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(columnDefinition = "TEXT")
    private String personalStatement;

    private String youtubeLink;

    private String personalLink;

    private String twitterLink;


    @Builder
    public Member(String name, String loginId, String email, String password, String nickName,
            LocalDate birthDay, Sex sex) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.birthDay = birthDay;
        this.sex = sex;
        this.loginId = loginId;
        this.authority = Authority.NORMAL;
        this.youtubeLink=null;
        this.personalStatement = null;
        this.personalLink =null;
        this.twitterLink = null;
        this.accessUrl = "";
    }

    public void changeAccessUrl(String accessUrl){
        if(accessUrl!=null){
            this.accessUrl = accessUrl;
        }
    }

    public void changeProfile(final String youtubeLink,final String personalLink,final String twitterLink,
            final String personalStatement,final String nickName){

        this.personalStatement = personalStatement;
        this.youtubeLink = youtubeLink;
        this.personalLink = personalLink;
        this.twitterLink = twitterLink;

        if(nickName!=null){
            this.nickName = nickName;
        }
    }

}
