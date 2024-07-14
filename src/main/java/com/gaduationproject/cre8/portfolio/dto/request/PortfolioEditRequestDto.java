package com.gaduationproject.cre8.portfolio.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Setter
public class PortfolioEditRequestDto {

    @NotNull(message = "포트폴리오 아이디는 공백이 될 수 없습니다")
    @Schema(description = "포트폴리오 아이디를 입력합니다",example = "1")
    private Long portfolioId;


    @Schema(description = "포트폴리오 상위 태그 번호를 입력합니다",example = "2")
    private Long workFieldId;

    @Schema(description = "포트폴리오 하위 태그 리스트 번호를 입력합니다")
    private List<Long> workFieldChildTagId = new ArrayList<>();

    @Schema(description = "포트폴리오 용 이미지를 등록합니다")
    private List<MultipartFile> multipartFileList = new ArrayList<>();

    @Schema(description = "작품에 대한 설명을 입력합니다")
    private String description;

}
