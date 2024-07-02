package com.gaduationproject.cre8.portfolio.dto.request;

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

    private Long portfolioId;

    private Long workFieldId;

    private List<Long> workFieldChildTagId = new ArrayList<>();

    private List<MultipartFile> multipartFileList = new ArrayList<>();

    private String description;

}
