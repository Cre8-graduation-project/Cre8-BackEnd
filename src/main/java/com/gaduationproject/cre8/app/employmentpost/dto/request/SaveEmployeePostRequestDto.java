package com.gaduationproject.cre8.app.employmentpost.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveEmployeePostRequestDto {

    @NotEmpty(message = "제목을 입력해 주세요")
    @Schema(description = "구직자 게시물에 대한 제목을 입력합니다",example = "월급 주실분 구합니다")
    private String title;


    @Schema(description = "작업 분야 상위 태그를 입력합니다",example = "2")
    private Long workFieldId;

    @Schema(description = "작업 분야 하위 태그 리스트를 입력합니다")
    private List<Long> workFieldChildTagId = new ArrayList<>();

    @Schema(description = "급여 지급 형식을 선택합니다(작업물 건 당 지급,작업물 분 당 지급,월급,기타)",example = "작업물 건 당 지급")
    private String paymentMethod;

    @Positive(message = "입력시 양수의 값을 입력해주세요")
    @Schema(description = "원하는 돈의 양을 입력해주세요",example = "15000")
    private Integer paymentAmount;

    @PositiveOrZero(message = "입력시 0이상의 값을 입력해주세요")
    @Schema(description = "당신의 경력을 입력해주세요",example = "15000")
    private Integer careerYear;

    @NotEmpty(message = "내용을 입력해 주세요")
    @Schema(description = "구직자 게시물에 대한 내용을 입력합니다",example = "나는 진짜 능력이 있어요")
    private String contents;

    @NotEmpty(message = "연락처를 입력해주세요(이메일,핸드폰)")
    @Schema(description = "연락처를 입력합니다",example = "010-1111-1111")
    private String contact;

    @Builder
    public SaveEmployeePostRequestDto(final String title, final Long workFieldId,
            final List<Long> workFieldChildTagId,
            final String paymentMethod, final Integer paymentAmount, final Integer careerYear,final String contents,final String contact) {

        this.title = title;
        this.workFieldId = workFieldId;
        this.workFieldChildTagId = workFieldChildTagId;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.careerYear = careerYear;
        this.contents = contents;
        this.contact = contact;
    }
}
