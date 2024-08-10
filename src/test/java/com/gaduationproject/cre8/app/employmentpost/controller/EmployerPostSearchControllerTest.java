package com.gaduationproject.cre8.app.employmentpost.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchWithCountResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostSearchResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostSearchWithCountResponseDto;
import com.gaduationproject.cre8.app.employmentpost.service.EmployeePostSearchService;
import com.gaduationproject.cre8.app.employmentpost.service.EmployerPostSearchService;
import com.gaduationproject.cre8.common.LocalDateDeserializer;
import com.gaduationproject.cre8.common.LocalDateSerializer;
import com.gaduationproject.cre8.common.TestSecurityConfig;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployeePostSearch;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployerPostSearch;
import com.gaduationproject.cre8.domain.employmentpost.type.EnrollDurationType;
import com.gaduationproject.cre8.domain.employmentpost.type.PaymentMethod;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.type.Sex;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(value = {EmployerPostSearchController.class, TestSecurityConfig.class})

class EmployerPostSearchControllerTest {



    @MockBean
    EmployerPostSearchService employerPostSearchService;


    private MockMvc mockMvc;
    private Gson gson;

    @Autowired
    private WebApplicationContext context;

    private static final String EMPLOYER_SEARCH_URL = "/api/v1/employer-posts/search";

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

    }

    @Test
    @DisplayName("구직 게시글 검색")
    public void 구직_게시글_검색() throws Exception {

        //given
        Member member = Member.builder()
                .name("지누")
                .sex(Sex.M)
                .birthDay(LocalDate.of(2023,1,1))
                .build();

        EmployerPost employerPost = EmployerPost.builder()
                .paymentMethod(PaymentMethod.ELSE)
                .title("제목1")
                .hopeCareerYear(5)
                .enrollDurationType(EnrollDurationType.ALWAYS)
                .member(member)
                .contents("내용1")
                .build();

        EmployerPostSearchResponseDto employerPostSearchResponseDto = EmployerPostSearchResponseDto
                .of(employerPost, List.of("태그1","태그2"));

        given(employerPostSearchService.searchEmployerPost(any(EmployerPostSearch.class),any(
                Pageable.class))).willReturn(EmployerPostSearchWithCountResponseDto.of(3L,
                List.of(employerPostSearchResponseDto),1));


        //when
        final ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.get(EMPLOYER_SEARCH_URL)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print());


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.employerPostSearchResponseDtoList[0].title").value("제목1"));
    }

}