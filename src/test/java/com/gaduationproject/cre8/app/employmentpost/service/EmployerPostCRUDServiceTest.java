package com.gaduationproject.cre8.app.employmentpost.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gaduationproject.cre8.app.employmentpost.dto.request.EditEmployeePostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.request.EditEmployerPostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.request.SaveEmployeePostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.request.SaveEmployerPostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostResponseDto;
import com.gaduationproject.cre8.app.portfolio.dto.response.PortfolioSimpleResponseDto;
import com.gaduationproject.cre8.app.portfolio.service.PortfolioService;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePostWorkFieldChildTag;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPostWorkFieldChildTag;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployeePostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployeePostWorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployerPostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployerPostWorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.employmentpost.type.EnrollDurationType;
import com.gaduationproject.cre8.domain.employmentpost.type.PaymentMethod;
import com.gaduationproject.cre8.domain.member.editor.MemberEditor;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioImage;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldSubCategory;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldTagRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class EmployerPostCRUDServiceTest {


    @InjectMocks
    EmployerPostCRUDService employerPostCRUDService;

    @Mock
    WorkFieldTagRepository workFieldTagRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    WorkFieldChildTagRepository workFieldChildTagRepository;

    @Mock
    EmployerPostRepository employerPostRepository;

    @Mock
    EmployerPostWorkFieldChildTagRepository employerPostWorkFieldChildTagRepository;

    @Mock
    PortfolioService portfolioService;


    @Test
    @DisplayName("구인 게시글 저장")
    public void 구인_게시글_저장(){

        //given
        WorkFieldTag workFieldTag1 = WorkFieldTag.builder()
                .name("작업분야1")
                .build();

        ReflectionTestUtils.setField(workFieldTag1,"id",1L);

        WorkFieldSubCategory workFieldSubCategory1 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag1)
                .name("카테고리1")
                .build();

        WorkFieldChildTag workFieldChildTag1 = WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1)
                .build();

        WorkFieldChildTag workFieldChildTag2 = WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1)
                .build();


        Member member = Member.builder().loginId("dionisos198")
                .email("dionisos198@naver.com")
                .password("ppp")
                .sex(Sex.M)
                .nickName("dionisos198")
                .birthDay(LocalDate.of(2023,1,1))
                .name("이진우")
                .build();

        given(workFieldTagRepository.findById(1L)).willReturn(
                Optional.of(WorkFieldTag.builder().name("작업분야1").build()));
        given(memberRepository.findMemberByLoginId(eq("dionisos198"))).willReturn(Optional.of(member));
        given(workFieldChildTagRepository.findById(eq(1L))).willReturn(Optional.of(workFieldChildTag1));
        given(workFieldChildTagRepository.findById(eq(2L))).willReturn(Optional.of(workFieldChildTag2));


        //when
        employerPostCRUDService.saveEmployerPost("dionisos198",
                SaveEmployerPostRequestDto.builder()
                        .hopeCareerYear(3)
                        .workFieldId(1L)
                        .workFieldChildTagId(List.of(1L,2L))
                        .title("사람구해요-제목")
                        .contents("사람구해요-내용")
                        .contact("dionisos198@naver.com")
                        .paymentAmount(1000)
                        .paymentMethod(PaymentMethod.MONTH.getName())
                        .companyName("회사1")
                        .enrollDurationType(EnrollDurationType.ALWAYS.getName())
                        .deadLine(null)
                        .build());



        //then
        verify(employerPostRepository,times(1)).save(any(EmployerPost.class));
        verify(employerPostWorkFieldChildTagRepository,times(2)).save(any(
                EmployerPostWorkFieldChildTag.class));

    }

    @Test
    @DisplayName("구직 게시글 수정")
    public void 구직_게시글_수정(){

        //given
        WorkFieldTag workFieldTag1 = WorkFieldTag.builder()
                .name("작업분야1")
                .build();


        ReflectionTestUtils.setField(workFieldTag1,"id",1L);

        WorkFieldSubCategory workFieldSubCategory1 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag1)
                .name("카테고리1")
                .build();

        WorkFieldChildTag workFieldChildTag1 = WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1)
                .build();

        WorkFieldChildTag workFieldChildTag2 = WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1)
                .build();


        Member member = Member.builder().loginId("dionisos198")
                .email("dionisos198@naver.com")
                .password("ppp")
                .sex(Sex.M)
                .nickName("dionisos198")
                .birthDay(LocalDate.of(2023,1,1))
                .name("이진우")
                .build();

        EmployerPost employerPost = EmployerPost.builder()
                .member(member)
                .workFieldTag(workFieldTag1)
                .hopeCareerYear(5)
                .title("수정 전 제목")
                .contact("ddd")
                .contents("수정 전 내용")
                .companyName("회사")
                .enrollDurationType(EnrollDurationType.DEAD_LINE)
                .deadLine(LocalDate.of(2023,1,1))
                .contact("dionisos198")
                .build();

        given(employerPostRepository.findById(1L)).willReturn(Optional.of(employerPost));
        given(workFieldTagRepository.findById(1L)).willReturn(Optional.of(WorkFieldTag.builder().name("작업분야1").build()));
        given(workFieldChildTagRepository.findById(eq(1L))).willReturn(Optional.of(workFieldChildTag1));
        given(workFieldChildTagRepository.findById(eq(2L))).willReturn(Optional.of(workFieldChildTag2));
        doNothing().when(employerPostWorkFieldChildTagRepository).deleteByEmployerPost(any(
                EmployerPost.class));



        //when
        employerPostCRUDService.updateEmployerPost("dionisos198",
                EditEmployerPostRequestDto.builder()
                        .employerPostId(1L)
                        .workFieldChildTagId(List.of(1L,2L))
                        .workFieldId(1L)
                        .title("수정 후 제목")
                        .contents("수정 후 내용")
                        .contact("여기로")
                        .hopeCareerYear(3)
                        .paymentAmount(300)
                        .paymentMethod(PaymentMethod.MONTH.getName())
                        .enrollDurationType(EnrollDurationType.ALWAYS.getName())
                        .deadLine(null)
                        .build());




        //then
        verify(employerPostWorkFieldChildTagRepository,times(2)).save(any(
                EmployerPostWorkFieldChildTag.class));
        assertThat(employerPost.getBasicPostContent().getTitle()).isEqualTo("수정 후 제목");

    }

    @Test
    @DisplayName("구직 게시글 단일 조회")
    public void 구직_게시글_단일_조회(){

        //given
        WorkFieldTag workFieldTag1 = WorkFieldTag.builder()
                .name("작업분야1")
                .build();


        ReflectionTestUtils.setField(workFieldTag1,"id",1L);

        WorkFieldSubCategory workFieldSubCategory1 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag1)
                .name("카테고리1")
                .build();

        WorkFieldChildTag workFieldChildTag1 = WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1)
                .name("자식태그1")
                .build();

        WorkFieldChildTag workFieldChildTag2 = WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1)
                .name("자식태그2")
                .build();


        Member member = Member.builder().loginId("dionisos198")
                .email("dionisos198@naver.com")
                .password("ppp")
                .sex(Sex.M)
                .nickName("dionisos198")
                .birthDay(LocalDate.of(2023,1,1))
                .name("이진우")
                .build();

        member.edit(MemberEditor.builder().accessUrl("access").build());

        ReflectionTestUtils.setField(member,"id",1L);

        EmployerPost employerPost = EmployerPost.builder()
                .member(member)
                .workFieldTag(workFieldTag1)
                .hopeCareerYear(5)
                .title("수정 전 제목")
                .contact("ddd")
                .contents("수정 전 내용")
                .paymentMethod(PaymentMethod.MONTH)
                //               .paymentAmount(3000)
                .enrollDurationType(EnrollDurationType.ALWAYS)
                .deadLine(null)
                .companyName("회사냥")
                .contact("dionisos198@naver.com")
                .numberOfEmployee(3)
                .build();



        EmployerPostWorkFieldChildTag employerPostWorkFieldChildTag1 = EmployerPostWorkFieldChildTag.builder()
                .employerPost(employerPost)
                .workFieldChildTag(workFieldChildTag1)
                .build();

        EmployerPostWorkFieldChildTag employerPostWorkFieldChildTag2 = EmployerPostWorkFieldChildTag.builder()
                .employerPost(employerPost)
                .workFieldChildTag(workFieldChildTag2)
                .build();



        Portfolio portfolio1 = Portfolio.builder().member(member).build();
        portfolio1.getPortfolioImageList().add(
                PortfolioImage.builder().originalName("원래1").portfolio(portfolio1).accessUrl("접근1").build());
        portfolio1.getPortfolioImageList().add(PortfolioImage.builder().originalName("원래2").portfolio(portfolio1).accessUrl("접근2").build());

        Portfolio portfolio2 = Portfolio.builder().member(member).build();
        portfolio2.getPortfolioImageList().add(PortfolioImage.builder().originalName("원래3").portfolio(portfolio1).accessUrl("접근3").build());

        Portfolio portfolio3 = Portfolio.builder().member(member).build();

        given(employerPostRepository.findByIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag(1L)).willReturn(Optional.of(employerPost));





        //when
        EmployerPostResponseDto employerPostResponseDto = employerPostCRUDService.showEmployerPost(
                1L);

        //then

        assertThat(employerPostResponseDto.getTagPostResponseDto().getWorkFieldTagName()).isEqualTo("작업분야1");
        assertThat(employerPostResponseDto.getTagPostResponseDto().getSubCategoryWithChildTagResponseDtoList()).size().isEqualTo(1);
        assertThat(employerPostResponseDto.getTagPostResponseDto().getSubCategoryWithChildTagResponseDtoList().get(0).
                getSubCategoryName()).isEqualTo("카테고리1");
        assertThat(employerPostResponseDto.getTagPostResponseDto().getSubCategoryWithChildTagResponseDtoList().get(0).
                getChildTagName().size()).isEqualTo(2);
        assertThat(employerPostResponseDto.getWriterAccessUrl()).isEqualTo("access");


    }


    @Test
    @DisplayName("구직 게시글 삭제")
    public void 구직_게시글_삭제(){

        //given
        WorkFieldTag workFieldTag1 = WorkFieldTag.builder()
                .name("작업분야1")
                .build();


        ReflectionTestUtils.setField(workFieldTag1,"id",1L);

        WorkFieldSubCategory workFieldSubCategory1 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag1)
                .name("카테고리1")
                .build();

        WorkFieldChildTag workFieldChildTag1 = WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1)
                .name("자식태그1")
                .build();

        WorkFieldChildTag workFieldChildTag2 = WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1)
                .name("자식태그2")
                .build();


        Member member = Member.builder().loginId("dionisos198")
                .email("dionisos198@naver.com")
                .password("ppp")
                .sex(Sex.M)
                .nickName("dionisos198")
                .birthDay(LocalDate.of(2023,1,1))
                .name("이진우")
                .build();

        member.edit(MemberEditor.builder().accessUrl("access").build());

        ReflectionTestUtils.setField(member,"id",1L);

        EmployerPost employerPost = EmployerPost.builder()
                .member(member)
                .workFieldTag(workFieldTag1)
                .hopeCareerYear(5)
                .title("수정 전 제목")
                .contact("ddd")
                .contents("수정 전 내용")
                .paymentMethod(PaymentMethod.MONTH)
                //               .paymentAmount(3000)
                .enrollDurationType(EnrollDurationType.ALWAYS)
                .deadLine(null)
                .build();



        EmployerPostWorkFieldChildTag employerPostWorkFieldChildTag1 = EmployerPostWorkFieldChildTag.builder()
                .employerPost(employerPost)
                .workFieldChildTag(workFieldChildTag1)
                .build();

        EmployerPostWorkFieldChildTag employerPostWorkFieldChildTag2 = EmployerPostWorkFieldChildTag.builder()
                .employerPost(employerPost)
                .workFieldChildTag(workFieldChildTag2)
                .build();




        given(employerPostRepository.findById(1L)).willReturn(Optional.of(employerPost));




        //when
        employerPostCRUDService.deleteEmployerPost("dionisos198",1L);

        //then

        verify(employerPostWorkFieldChildTagRepository,times(1)).deleteByEmployerPost(any(EmployerPost.class));
        verify(employerPostRepository,times(1)).deleteById(anyLong());

    }

}