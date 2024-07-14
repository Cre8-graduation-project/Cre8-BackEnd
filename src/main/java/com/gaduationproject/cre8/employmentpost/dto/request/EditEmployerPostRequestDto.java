package com.gaduationproject.cre8.employmentpost.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditEmployerPostRequestDto {

    @NotNull(message = "수정할 postID 를 입력해주세요")
    @Schema(description = "수정할 구인자 게시글 아이디",example = "1")
    private Long employerPostId;

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
    @Schema(description = "줄 수 있는 돈의 양을 입력해주세요",example = "15000")
    private Integer paymentAmount;

    @NotEmpty(message = "회사 혹은 개인의 유튜브 이름 을 입력해주세요")
    @Schema(description = "회사 or 개인의 유튜브 이름 등",example = "풍월량 편집자 구해요")
    private String companyName;

    @PositiveOrZero(message = "0 이상의 값을 입력해주세요")
    @Schema(description = "구할 사람의 수",example = "0")
    private Integer numberOfEmployee;

    @Schema(description = "등록 마감 방식(채용 시 마감,마감일 지정,상시 채용)",example = "0")
    private String enrollDurationType;

    @Schema(description = "마감일 지정 방식일 경우 DeadLine 입력",example = "0")
    private LocalDate deadLine;

    @PositiveOrZero(message = "0 이상의 값을 입력해주세요")
    @Schema(description = "희망 경력 최소 연도(없으면 NULL 도 괜찮고 0 도 괜찮음)",example = "0")
    private Integer hopeCareerYear;

    @NotEmpty(message = "내용을 입력해주세요")
    @Schema(description = "구인자 게시글 내용 입력",example = "0")
    private String contents;


    @Builder
    public EditEmployerPostRequestDto(final Long employerPostId, final Long workFieldId,final String title, final List<Long> workFieldChildTagId,
            final String paymentMethod, final Integer paymentAmount,final String companyName, final Integer numberOfEmployee,
            final String enrollDurationType,final LocalDate deadLine, final Integer hopeCareerYear,final String contents) {
        this.employerPostId = employerPostId;
        this.workFieldId = workFieldId;
        this.title = title;
        this.workFieldChildTagId = workFieldChildTagId;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.companyName = companyName;
        this.numberOfEmployee = numberOfEmployee;
        this.enrollDurationType = enrollDurationType;
        this.deadLine = deadLine;
        this.hopeCareerYear = hopeCareerYear;
        this.contents = contents;
    }

}
