package com.gaduationproject.cre8.domain.member.entity;

import com.gaduationproject.cre8.domain.member.editor.MemberEditor;
import com.gaduationproject.cre8.domain.member.type.Sex;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
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

    public MemberEditor.MemberEditorBuilder toEditor(){

        return MemberEditor.builder()
                .accessUrl(this.accessUrl)
                .personalStatement(this.personalStatement)
                .youtubeLink(this.youtubeLink)
                .personalLink(this.personalLink)
                .twitterLink(this.twitterLink)
                .nickName(this.nickName);

    }

    public void edit(final MemberEditor memberEditor){

        this.accessUrl = memberEditor.getAccessUrl();
        this.personalStatement = memberEditor.getPersonalStatement();
        this.youtubeLink = memberEditor.getYoutubeLink();
        this.personalLink = memberEditor.getPersonalLink();
        this.twitterLink = memberEditor.getTwitterLink();
        this.nickName = memberEditor.getNickName();

    }




}
