package com.gaduationproject.cre8.app.portfolio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gaduationproject.cre8.app.member.service.ProfileService;
import com.gaduationproject.cre8.app.portfolio.dto.S3UploadPortfolioImageCommitEvent;
import com.gaduationproject.cre8.app.portfolio.dto.S3UploadPortfolioImageRollbackEvent;
import com.gaduationproject.cre8.app.portfolio.dto.request.PortfolioEditRequestDto;
import com.gaduationproject.cre8.app.portfolio.dto.response.PortfolioResponseDto;
import com.gaduationproject.cre8.app.portfolio.dto.response.PortfolioSimpleResponseDto;
import com.gaduationproject.cre8.domain.member.editor.MemberEditor;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioImage;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioWorkFieldChildTag;
import com.gaduationproject.cre8.domain.portfolio.repository.PortfolioImageRepository;
import com.gaduationproject.cre8.domain.portfolio.repository.PortfolioRepository;
import com.gaduationproject.cre8.domain.portfolio.repository.PortfolioWorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldSubCategory;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldTagRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @InjectMocks
    PortfolioService portfolioService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PortfolioRepository portfolioRepository;

    @Mock
    WorkFieldTagRepository workFieldTagRepository;

    @Mock
    WorkFieldChildTagRepository workFieldChildTagRepository;

    @Mock
    PortfolioWorkFieldChildTagRepository portfolioWorkFieldChildTagRepository;

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @Mock
    PortfolioImageRepository portfolioImageRepository;

    private Member getChangedMember(){
        Member member = Member.builder()
                .name("이진우")
                .loginId("dionisos198")
                .email("dionisos198@naver.com")
                .password("password")
                .sex(Sex.M)
                .nickName("dionisos198")
                .birthDay(LocalDate.of(2023,1,1))
                .build();

        ReflectionTestUtils.setField(member,"id",1L);


        MemberEditor.MemberEditorBuilder memberEditorBuilder = member.toEditor();
        member.edit(memberEditorBuilder.nickName("dionisos198")
                .personalStatement("나는 박살이다")
                .personalLink("www.jinu.com")
                .youtubeLink(null)
                .twitterLink(null)
                .build());



        return member;
    }

    @Test
    @DisplayName("포트폴리오 저장")
    public void 포트폴리오_저장(){

        //given
        given(memberRepository.findMemberByLoginId(eq("dionisos198"))).willReturn(Optional.of(getChangedMember()));

        //when
        portfolioService.savePortfolio("dionisos198");

        //then
        verify(portfolioRepository,times(1)).save(any());

    }

    @Test
    @DisplayName("포트폴리오 수정")
    public void 포트폴리오_수정(){

        //given
        Member member = getChangedMember();
        Portfolio portfolio = Portfolio.builder().member(member).build();
        ReflectionTestUtils.setField(portfolio,"id",1L);
        WorkFieldTag workFieldTag = WorkFieldTag.builder().name("작업분야1").build();
        ReflectionTestUtils.setField(workFieldTag,"id",1L);
        WorkFieldSubCategory workFieldSubCategory = WorkFieldSubCategory.builder()
                        .workFieldTag(workFieldTag)
                .name("하위분야")
                                .build();
        WorkFieldChildTag workFieldChildTag1 = WorkFieldChildTag.builder()
                        .workFieldSubCategory(workFieldSubCategory)
                                .name("자식태그1")
                                        .build();
        WorkFieldChildTag workFieldChildTag2 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory)
                .name("자식태그2")
                .build();
        WorkFieldChildTag workFieldChildTag3 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory)
                .name("자식태그3")
                .build();
        given(portfolioRepository.findById(eq(1L))).willReturn(Optional.of(portfolio));
        given(workFieldTagRepository.findById(eq(1L))).willReturn(Optional.of(workFieldTag));
        given(workFieldChildTagRepository.findById(eq(1L))).willReturn(Optional.of(workFieldChildTag1));
        given(workFieldChildTagRepository.findById(eq(2L))).willReturn(Optional.of(workFieldChildTag2));
        given(workFieldChildTagRepository.findById(eq(3L))).willReturn(Optional.of(workFieldChildTag3));
        doNothing().when(portfolioWorkFieldChildTagRepository).deleteByPortfolio(any());
        doNothing().when(applicationEventPublisher).publishEvent(any(
                S3UploadPortfolioImageRollbackEvent.class));
        doNothing().when(applicationEventPublisher).publishEvent(any(
                S3UploadPortfolioImageCommitEvent.class));






        //when
        portfolioService.updatePortfolio("dionisos198", PortfolioEditRequestDto.builder().portfolioId(1L)
                        .deletePortfolioImageList(List.of())
                        .description("수정 후 설명")
                        .workFieldId(1L)
                        .workFieldChildTagId(List.of(1L,2L,3L))
                        .multipartFileList(List.of())
                        .build());



        //then
        assertThat(portfolio.getDescription()).isEqualTo("수정 후 설명");

    }


    @Test
    @DisplayName("포트폴리오 삭제")
    public void 포트폴리오_삭제(){

        //given
        Member member = getChangedMember();
        Portfolio portfolio = Portfolio.builder().member(member).build();
        ReflectionTestUtils.setField(portfolio,"id",1L);
        given(portfolioRepository.findById(eq(1L))).willReturn(Optional.of(portfolio));
        given(portfolioImageRepository.findByPortfolio(portfolio)).willReturn(List.of());
        doNothing().when(applicationEventPublisher).publishEvent(any(S3UploadPortfolioImageCommitEvent.class));




        //when

        portfolioService.deletePortfolio("dionisos198",1L);

        //then


        verify(portfolioWorkFieldChildTagRepository,times(1)).deleteByPortfolio(portfolio);
        verify(portfolioRepository,times(1)).delete(portfolio);



    }

    @Test
    @DisplayName("포트폴리오 리스트 조회")
    public void 포트폴리오_리스트_조회(){

        //given
        Member member = getChangedMember();
        ReflectionTestUtils.setField(member,"id",1L);


        Portfolio portfolio1 = Portfolio.builder().member(member).build();
        ReflectionTestUtils.setField(portfolio1,"id",1L);
        portfolio1.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio1)
                .accessUrl("엑세스url1").originalName("원래1").build());
        portfolio1.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio1)
                .accessUrl("엑세스url2").originalName("원래2").build());

        Portfolio portfolio2 = Portfolio.builder().member(member).build();
        ReflectionTestUtils.setField(portfolio2,"id",2L);

        given(portfolioRepository.findByMemberIdWithFetchPortfolioImage(eq(1L))).willReturn(List.of(portfolio1,portfolio2));
        given(memberRepository.findById(eq(1L))).willReturn(Optional.of(member));





        //when

        List<PortfolioSimpleResponseDto> portfolioSimpleResponseDtoList = portfolioService.showPortfolioList(
                member.getId());

        //then


        assertThat(portfolioSimpleResponseDtoList.size()).isEqualTo(2);
        assertThat(portfolioSimpleResponseDtoList.get(0).getAccessUrl()).isEqualTo("엑세스url1");
        assertThat(portfolioSimpleResponseDtoList.get(1).getAccessUrl()).isNull();


    }

    @Test
    @DisplayName("포트폴리오 단일 조회")
    public void 포트폴리오_단일_조회(){

        //given
        Member member = getChangedMember();
        ReflectionTestUtils.setField(member,"id",1L);


        Portfolio portfolio1 = Portfolio.builder().member(member).build();
        ReflectionTestUtils.setField(portfolio1,"id",1L);
        portfolio1.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio1)
                .accessUrl("엑세스url1").originalName("원래1").build());
        portfolio1.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio1)
                .accessUrl("엑세스url2").originalName("원래2").build());

        WorkFieldTag workFieldTag = WorkFieldTag.builder().name("작업분야1").build();
        portfolio1.changeWorkFieldTagAndDescription(workFieldTag,"하잉");
        WorkFieldSubCategory workFieldSubCategory = WorkFieldSubCategory.builder().workFieldTag(workFieldTag).name("서브카테고리").build();
        WorkFieldChildTag workFieldChildTag1 = WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory).name("하위1").build();
        WorkFieldChildTag workFieldChildTag2 = WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory).name("하위1").build();
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag1 = PortfolioWorkFieldChildTag
                .builder().portfolio(portfolio1).workFieldChildTag(workFieldChildTag1).build();
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag2 = PortfolioWorkFieldChildTag
                .builder().portfolio(portfolio1).workFieldChildTag(workFieldChildTag2).build();


        given(portfolioRepository.findByPortfolioIdWithFetchImageAndWorkFieldTag(eq(1L))).willReturn(Optional.of(portfolio1));
        given(portfolioWorkFieldChildTagRepository.findByPortfolioIdWithFetchWorkFieldChildTag(eq(1L)))
                .willReturn(List.of(portfolioWorkFieldChildTag1,portfolioWorkFieldChildTag2));





        //when

        PortfolioResponseDto portfolioResponseDto = portfolioService.showPortfolio(
                1L);


        //then
        assertThat(portfolioResponseDto.getPortfolioImageResponseDtoList().size()).isEqualTo(2);
        assertThat(portfolioResponseDto.getTagName().size()).isEqualTo(3);
        assertThat(portfolioResponseDto.getDescription()).isEqualTo("하잉");


    }


}