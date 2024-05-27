package com.gaduationproject.cre8.member.entity;

import com.gaduationproject.cre8.member.type.Sex;
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

    @Column(nullable = false)
    private String password;

    @Column(length = 20,nullable = false,unique = true)
    private String nickName;

    @Column(nullable = false)
    private LocalDate birthDay;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;


    @Builder
    public Member(String name, String email, String password, String nickName,
            LocalDate birthDay, Sex sex) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.birthDay = birthDay;
        this.sex = sex;
    }
}