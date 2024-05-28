package com.gaduationproject.cre8.member.dto;

import com.gaduationproject.cre8.common.response.error.checkEnumManOrWoman.CheckManOrWoman;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.type.Sex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSignUpRequestDto {


    @NotBlank(message = "이름은 공백이 될 수 없습니다")
    @Size(max=20,message = "이름은 최대 20글자를 초과할 수 없습니다.")
    private String name;

    @NotBlank(message = "이메일은 공백이 될 수 없습니다")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    private String email;

    @NotBlank(message = "비밀번호는 공백이 돌 수 없습니다")
    @Size(min=5,max = 20 , message = "최소 5글자에서 최대 20글자를 입력해주세요")
    private String password;

    @CheckManOrWoman
    private Sex sex;

    @NotBlank(message = "사용할 닉네임을 입력해주세요")
    @Size(min = 5, max = 20, message = "최소 5글자에서 최대 20글자를 입력해주세요")
    private String nickName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
