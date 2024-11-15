package com.gaduationproject.cre8.app.portfolio.dto.response;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioResponseDto {

    private List<String> tagName = new ArrayList<>();
    private List<PortfolioImageResponseDto> portfolioImageResponseDtoList = new ArrayList<>();
    private String description;
    private Long recentEmployeePostId;

    public static PortfolioResponseDto from(List<String> tagName, Portfolio portfolio,
            Long recentEmployeePostId){

        return new PortfolioResponseDto(tagName,portfolio.getPortfolioImageList().stream().map(PortfolioImageResponseDto::of).collect(
                Collectors.toList()), portfolio.getDescription(),recentEmployeePostId);

    }

}
