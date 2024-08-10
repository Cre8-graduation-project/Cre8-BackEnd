package com.gaduationproject.cre8.app.employmentpost.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchWithCountResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostSearchWithCountResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePostWorkFieldChildTag;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployeePostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployerPostRepository;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployeePostSearch;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployerPostSearch;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldTagRepository;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EmployeePostSearchServiceTest {

    @InjectMocks
    EmployeePostSearchService employeePostSearchService;

    @Mock
    EmployeePostRepository employeePostRepository;


    @Test
    @DisplayName("검색 기반 리스트 조회 확인")
    public void 검색_기반_구직글_게시글_리스트_조회(){

        Member member = Member.builder().name("이름")
                .sex(Sex.M)
                .email("dionisos198")
                .birthDay(LocalDate.of(2023,1,1))
                .loginId("dionisos198")
                .build();

        //given

        EmployeePost employeePost1 = EmployeePost.builder()
                .title("제목1")
                .workFieldTag(WorkFieldTag.builder().name("작업태그1").build())
                .member(member)
                .build();

        EmployeePostWorkFieldChildTag employeePostWorkFieldChildTag = EmployeePostWorkFieldChildTag
                .builder().employeePost(employeePost1).
                workFieldChildTag(WorkFieldChildTag.builder().workFieldSubCategory(null).name("분야1").build()).build();

        EmployeePost employeePost2 = EmployeePost.builder()
                .title("제목2")
                .member(member)
                .build();
        EmployeePost employeePost3 = EmployeePost.builder()
                .title("제목3")
                .member(member)
                .build();

        Pageable pageable = PageRequest.of(0,2);
        EmployeePostSearch employeePostSearch = new EmployeePostSearch(1L, List.of(1L,2L),3,5);

        given(employeePostRepository.showEmployeePostListWithPage(employeePostSearch, pageable))
                .willReturn(new PageImpl<>(List.of(employeePost1, employeePost2), PageRequest.of(0,2), 3));

        //when
        EmployeePostSearchWithCountResponseDto employeePostSearchWithCountResponseDto = employeePostSearchService.searchEmployeePost(
                employeePostSearch, pageable);


        //then
        assertThat(employeePostSearchWithCountResponseDto.getTotalCount()).isEqualTo(3);
        assertThat(employeePostSearchWithCountResponseDto.getTotalPages()).isEqualTo(2);
        assertThat(employeePostSearchWithCountResponseDto.getEmployeePostSearchResponseDtoList().size()).isEqualTo(2);


    }


}