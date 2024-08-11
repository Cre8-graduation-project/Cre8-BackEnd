package com.gaduationproject.cre8.app.portfolio.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.gaduationproject.cre8.app.portfolio.dto.request.PortfolioEditRequestDto;
import com.gaduationproject.cre8.app.portfolio.dto.response.PortfolioResponseDto;
import com.gaduationproject.cre8.app.portfolio.dto.response.PortfolioSimpleResponseDto;
import com.gaduationproject.cre8.app.portfolio.service.PortfolioService;
import com.gaduationproject.cre8.common.LocalDateDeserializer;
import com.gaduationproject.cre8.common.LocalDateSerializer;
import com.gaduationproject.cre8.common.TestSecurityConfig;
import com.gaduationproject.cre8.common.WithMockCustomUser;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioImage;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;



@WebMvcTest(value = {PortfolioController.class, TestSecurityConfig.class})
class PortfolioControllerTest {


    @MockBean
    PortfolioService portfolioService;

    private MockMvc mockMvc;
    private Gson gson;

    @Autowired
    private WebApplicationContext context;

    private static final String PORTFOLIO_URL = "/api/v1/portfolios";

    @BeforeEach
    public void init() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());

        gson = gsonBuilder.setPrettyPrinting().create();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("utf-8", true))
                .apply(springSecurity())
                .build();

        //mockMvc=MockMvcBuilders.
//        mockMvc = MockMvcBuilders
//                .setControllerAdvice(new RestExceptionHandler())
//                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
//                .build();


    }

    private Member makeDefaultMember(){

        Member member =  Member.builder()
                .nickName("지누")
                .sex(Sex.M)
                .email("dionisos198@naver.com")
                .password("kkk")
                .loginId("dionisos198")
                .birthDay(LocalDate.now())
                .name("이진우")
                .build();

        ReflectionTestUtils.setField(member,"id",1L);

        return member;

    }

    @Test
    @DisplayName("포트폴리오 저장")
    @WithMockCustomUser(loginId = "dionisos198", name = "이진우")
    void 포트폴리오_저장() throws Exception{

        //given
        given(portfolioService.savePortfolio("dionisos198")).willReturn(1L);

        //when
        final ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.post(PORTFOLIO_URL)
                                .with(csrf())

                )
                .andDo(print());


        //then
        resultActions.andExpect(status().isCreated())
                .andDo(print());

    }

    @Test
    @DisplayName("포트폴리오 수정")
    @WithMockCustomUser(loginId = "dionisos198", name = "이진우")
    void 포트폴리오_수정() throws Exception {
        //given
        PortfolioEditRequestDto portfolioEditRequestDto = PortfolioEditRequestDto.builder()
                .portfolioId(1L)
                .build();

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put(PORTFOLIO_URL)
                        .param("portfolioId",
                                String.valueOf(portfolioEditRequestDto.getPortfolioId()))
                .contentType(MediaType.MULTIPART_FORM_DATA)).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("포트폴리오 단일 조회")
    void 포트폴리오_단일_조회() throws Exception {

        //given
        Member member = makeDefaultMember();
        Portfolio portfolio = Portfolio.builder().member(member).build();
        portfolio.getPortfolioImageList().add(PortfolioImage.builder().originalName("원래1").accessUrl("ac1").build());
        portfolio.getPortfolioImageList().add(PortfolioImage.builder().originalName("원래2").accessUrl("ac2").build());
        WorkFieldTag workFieldTag = WorkFieldTag.builder().name("작업분야1").build();

        portfolio.changeWorkFieldTagAndDescription(workFieldTag,"하잉");
        given(portfolioService.showPortfolio(eq(1L))).willReturn(PortfolioResponseDto.from(List.of("작업분야1","자식태그1","자식태그2"),portfolio));


        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(PORTFOLIO_URL+"/{portfolioId}",1L)
             //   .with(csrf()))
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.description").value("하잉"))
                .andDo(print());

    }

    @Test
    @DisplayName("멤버 아이디 기반 포트폴리오 리스트 조회 ")
    void 포트폴리오_리스트_조회() throws Exception{

        //given
        Member member = makeDefaultMember();
        Portfolio portfolio = Portfolio.builder().member(member).build();
        portfolio.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio).accessUrl("access1").build());
        portfolio.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio).accessUrl("access2").build());

        Portfolio portfolio1 = Portfolio.builder().member(member).build();
        portfolio.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio1).accessUrl("access1").build());

        Portfolio portfolio2 = Portfolio.builder().member(member).build();


        given(portfolioService.showPortfolioList(eq(1L))).willReturn(List.of(PortfolioSimpleResponseDto.from(portfolio),
                        PortfolioSimpleResponseDto.from(portfolio1),
                        PortfolioSimpleResponseDto.from(portfolio2)));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(PORTFOLIO_URL+"/member/{memberId}",1L)
                //   .with(csrf()))
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()").value(3))
                .andExpect(jsonPath("$.data.[0].accessUrl").value("access1"));

    }

    @Test
    @DisplayName("포트폴리오 삭제")
    @WithMockCustomUser(loginId = "dionisos198", name = "이진우")
    void 포트폴리오_삭제() throws Exception{

        //given
        doNothing().when(portfolioService).deletePortfolio("dionisos198",1L);


        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(PORTFOLIO_URL+"/{portfolioId}",1L)
                //   .with(csrf()))
        ).andDo(print());

        resultActions.andExpect(status().isOk());


    }
}