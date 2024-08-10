package com.gaduationproject.cre8.app.employmentpost.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchWithCountResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostSearchWithCountResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePostWorkFieldChildTag;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPostWorkFieldChildTag;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployeePostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployerPostRepository;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployeePostSearch;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployerPostSearch;
import com.gaduationproject.cre8.domain.employmentpost.type.EnrollDurationType;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@ExtendWith(MockitoExtension.class)
class EmployerPostSearchServiceTest {

    @InjectMocks
    EmployerPostSearchService employerPostSearchService;

    @Mock
    EmployerPostRepository employerPostRepository;


    @Test
    @DisplayName("검색 기반 리스트 조회 확인")
    public void 검색_기반_구인글_게시글_리스트_조회(){

        Member member = Member.builder().name("이름")
                .sex(Sex.M)
                .email("dionisos198")
                .birthDay(LocalDate.of(2023,1,1))
                .loginId("dionisos198")
                .build();

        //given

        EmployerPost employerPost1 = EmployerPost.builder()
                .title("제목1")
                .workFieldTag(WorkFieldTag.builder().name("작업태그1").build())
                .member(member)
                .enrollDurationType(EnrollDurationType.ALWAYS)
                .build();

        EmployerPostWorkFieldChildTag employerPostWorkFieldChildTag = EmployerPostWorkFieldChildTag
                .builder().employerPost(employerPost1).
                workFieldChildTag(
                        WorkFieldChildTag.builder().workFieldSubCategory(null).name("분야1").build()).build();

        EmployerPost employerPost2 = EmployerPost.builder()
                .title("제목2")
                .member(member)
                .enrollDurationType(EnrollDurationType.ALWAYS)
                .build();
        EmployerPost employerPost3 = EmployerPost.builder()
                .title("제목3")
                .member(member)
                .enrollDurationType(EnrollDurationType.ALWAYS)
                .build();

        Pageable pageable = PageRequest.of(0,2);
        EmployerPostSearch employerPostSearch = new EmployerPostSearch(1L, List.of(1L,2L),3,5);

        given(employerPostRepository.showEmployerPostListWithPage(employerPostSearch, pageable))
                .willReturn(new PageImpl<>(List.of(employerPost1, employerPost2), PageRequest.of(0,2), 3));

        //when
        EmployerPostSearchWithCountResponseDto employerPostSearchWithCountResponseDto = employerPostSearchService.searchEmployerPost(
                employerPostSearch, pageable);


        //then
        assertThat(employerPostSearchWithCountResponseDto.getTotalCount()).isEqualTo(3);
        assertThat(employerPostSearchWithCountResponseDto.getTotalPages()).isEqualTo(2);
        assertThat(employerPostSearchWithCountResponseDto.getEmployerPostSearchResponseDtoList().size()).isEqualTo(2);


    }

}