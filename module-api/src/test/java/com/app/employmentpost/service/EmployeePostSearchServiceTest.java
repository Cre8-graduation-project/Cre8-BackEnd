package com.app.employmentpost.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.cre8.employmentpost.dto.response.EmployeePostSearchWithCountResponseDto;
import com.cre8.employmentpost.entity.EmployeePost;
import com.cre8.employmentpost.entity.EmployeePostWorkFieldChildTag;
import com.cre8.employmentpost.repository.EmployeePostRepository;
import com.cre8.employmentpost.search.EmployeePostSearch;
import com.cre8.employmentpost.service.EmployeePostSearchService;
import com.cre8.member.entity.Member;
import com.cre8.member.type.Sex;
import com.cre8.workfieldtag.entity.WorkFieldChildTag;
import com.cre8.workfieldtag.entity.WorkFieldTag;
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
                workFieldChildTag(
                        WorkFieldChildTag.builder().workFieldSubCategory(null).name("분야1").build()).build();

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