package com.gaduationproject.cre8.app.employmentpost.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gaduationproject.cre8.app.employmentpost.dto.request.EditEmployeePostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.request.EditEmployerPostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.request.SaveEmployeePostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.request.SaveEmployerPostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.SubCategoryWithChildTagResponseDto;
import com.gaduationproject.cre8.app.employmentpost.service.EmployeePostCRUDService;
import com.gaduationproject.cre8.app.employmentpost.service.EmployerPostCRUDService;
import com.gaduationproject.cre8.app.portfolio.dto.response.PortfolioSimpleResponseDto;
import com.gaduationproject.cre8.common.LocalDateDeserializer;
import com.gaduationproject.cre8.common.LocalDateSerializer;
import com.gaduationproject.cre8.common.TestSecurityConfig;
import com.gaduationproject.cre8.common.WithMockCustomUser;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.type.EnrollDurationType;
import com.gaduationproject.cre8.domain.employmentpost.type.PaymentMethod;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(value = {EmployerPostController.class, TestSecurityConfig.class})

class EmployerPostControllerTest {

    @MockBean
    EmployerPostCRUDService employerPostCRUDService;

    private MockMvc mockMvc;
    private Gson gson;

    @Autowired
    private WebApplicationContext context;

    private static final String EMPLOYER_URL = "/api/v1/employer/posts";

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
    @DisplayName("구인자 게시글 저장")
    @WithMockCustomUser(loginId = "dionisos198", name = "이진우")
    void 구직자_저장() throws Exception{

        //given
        doNothing().when(employerPostCRUDService).saveEmployerPost(eq("dionisos198"),
                any(SaveEmployerPostRequestDto.class));

        //when
        final ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.post(EMPLOYER_URL)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(SaveEmployerPostRequestDto.builder()
                                        .title("제목")
                                        .contact("내번호 알지?")
                                        .contents("내용")
                                        .companyName("fsdfsd")
                                        .build()))

                )
                .andDo(print());

        //then
        resultActions.andExpect(status().isCreated())
                .andDo(print());

    }

    @Test
    @DisplayName("구직자 게시글 수정")
    @WithMockCustomUser(loginId = "dionisos198", name = "이진우")
    void 구직자_수정() throws Exception{

        //given
        doNothing().when(employerPostCRUDService).updateEmployerPost(eq("dionisos198"),
                any(EditEmployerPostRequestDto.class));

        //when
        final ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.put(EMPLOYER_URL)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(EditEmployerPostRequestDto.builder()
                                        .employerPostId(1L)
                                        .title("제목")
                                        .contact("내번호 알지?")
                                        .companyName("www.fsfds.sfdfs")
                                        .contents("내용")
                                        .build()))

                )
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("구직자 게시글 삭제")
    @WithMockCustomUser(loginId = "dionisos198", name = "이진우")
    void 구직자_삭제() throws Exception{

        //given
        doNothing().when(employerPostCRUDService).deleteEmployerPost(eq("dionisos198"),
                anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.delete(EMPLOYER_URL+"/{postId}",1L)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());

    }
    @Test
    @DisplayName("구직자 게시글 단건 조회")
    void 구직자_게시글_단건조회() throws Exception{

        //given
        final List<SubCategoryWithChildTagResponseDto> subCategoryWithChildTagResponseDtoList= new ArrayList<>();
        subCategoryWithChildTagResponseDtoList.add(SubCategoryWithChildTagResponseDto.of("서브1",List.of("자식1","자식2")));
        subCategoryWithChildTagResponseDtoList.add(SubCategoryWithChildTagResponseDto.of("서브2",List.of("자식3")));

        Member member = Member.builder()
                .sex(Sex.M)
                .birthDay(LocalDate.of(2023,1,1))
                .build();

        final EmployerPost employerPost = EmployerPost.builder().member(member)
                .paymentMethod(PaymentMethod.ELSE)
                        .enrollDurationType(EnrollDurationType.ALWAYS)
                                .build();


        given(employerPostCRUDService.showEmployerPost(anyLong())).willReturn(
                EmployerPostResponseDto.of(subCategoryWithChildTagResponseDtoList,employerPost));

        //when
        final ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.get(EMPLOYER_URL+"/{postId}",1L)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.enrollDurationType").value("상시 채용"))
                .andDo(print());

    }


}