package com.gaduationproject.cre8.member.dto;

import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.type.Sex;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSignUpRequestDto {

    private String name;
    private String email;
    private String password;
    private Sex sex;
    private String nickName;
    private LocalDate birthDay;

    @Builder
    public MemberSignUpRequestDto(String name,String email, String password, Sex sex,String nickName,
             LocalDate birthDay){
        this.name = name;
        this.email = email;
        this.password = password;
        this.sex = sex;
        this.nickName = nickName;
        this.birthDay = birthDay;
    }

}
