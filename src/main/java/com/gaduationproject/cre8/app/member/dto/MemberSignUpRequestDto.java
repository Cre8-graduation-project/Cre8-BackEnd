package com.gaduationproject.cre8.app.member.dto;


import com.gaduationproject.cre8.domain.member.type.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "최종 회원가입을 하기 위한, 적용되면 회원가입이 완료되는 dto")
public class MemberSignUpRequestDto {


    @NotBlank(message = "이름은 공백이 될 수 없습니다")
    @Size(max=20,message = "이름은 최대 20글자를 초과할 수 없습니다.")
    @Schema(description = "가입하고자 하는 사용자의 이름",example = "이진우",maxLength = 20)
    private String name;


    @NotBlank(message = "아이디는 공백이 될 수 없습니다")
    @Size(max = 20, message = "최대 20 글자 미만으로 만들어주세요!")
    @Schema(description = "가입하고자 하는 사용자의 로그인 아이디",example = "blackbox0209",maxLength = 20)
    private String loginId;

    @NotBlank(message = "비밀번호는 공백이 돌 수 없습니다")
    @Size(min=5,max = 20 , message = "최소 5글자에서 최대 20글자를 입력해주세요")
    @Schema(description = "가입하고자 하는 사용자의 로그인 비밀번호",example = "kdkdkdk",maxLength = 20,minLength = 5)
    private String password;

    @NotNull(message = "남자와 여자 둘중에 하나를 선택해주세요")
    @Schema(description = "가입하고자 하는 사용자의 성별 M or W 로 입력",example = "M")
    private Sex sex;

    @NotBlank(message = "사용할 닉네임을 입력해주세요")
    @Size(min = 5, max = 20, message = "최소 5글자에서 최대 20글자를 입력해주세요")
    @Schema(description = "가입하고자 하는 사용자의 닉네임",example = "blackbox0209",minLength = 5,maxLength = 20)
    private String nickName;

    @NotNull(message = "생년월일을 입력해주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "가입하고자 하는 사용자의 생일",example = "1998-02-09")
    private LocalDate birthDay;

    @Email(message = "이메일 형식에 맞지 않습니다")
    @NotEmpty(message = "이메일을 입력해 주세요")
    @Schema(description = "가입하고자 하는 사용자의 이메일",example = "blackbox0209@naver.com")
    private String email;

    @Builder
    public MemberSignUpRequestDto(String name,String loginId, String password, Sex sex,String nickName,
             LocalDate birthDay, String email){

        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.sex = sex;
        this.nickName = nickName;
        this.birthDay = birthDay;
        this.email = email;
    }

}
