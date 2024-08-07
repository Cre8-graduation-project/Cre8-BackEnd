package com.gaduationproject.cre8.member.controller;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.gaduationproject.cre8.api.member.controller.MemberController;
import com.gaduationproject.cre8.common.LocalDateDeserializer;
import com.gaduationproject.cre8.common.LocalDateSerializer;
import com.gaduationproject.cre8.common.response.error.RestExceptionHandler;
import com.gaduationproject.cre8.api.member.dto.MemberSignUpRequestDto;
import com.gaduationproject.cre8.api.member.service.MemberSignUpService;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {
    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberSignUpService memberSignUpService;

    private MockMvc mockMvc;
    private Gson gson;

    private static final String MEMBER_SIGNUP_URL = "/api/v1/members";

    @BeforeEach
    public void init() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());

        gson = gsonBuilder.setPrettyPrinting().create();

        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    private MemberSignUpRequestDto memberSignUpRequestDto(final String nickName, final String password, final Sex sex, final LocalDate birthDay,
             final String loginId, final String name,final String email){
        return MemberSignUpRequestDto.builder()
                .nickName(nickName)
                .password(password)
                .email(email)
                .sex(sex)
                .birthDay(birthDay)
                .loginId(loginId)
                .name(name)
                .build();
    }

    @Test
    @DisplayName("정상적인 멤버 데이터 저장 상황")
    public void 멤버데이터_정상_저장() throws Exception {
        //given

        when(memberSignUpService.saveMember(any(MemberSignUpRequestDto.class))).thenReturn(1L);

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(MEMBER_SIGNUP_URL)
                        .content(gson.toJson(memberSignUpRequestDto("dionisos198","password",
                                Sex.M,LocalDate.of(2023,1,1),"dionisos198","이진우","dionisos198@naver.com")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isCreated());

    }


    @Test
    @DisplayName("비정상적인 멤버 데이터 저장 상황-로그인 아이디")
    public void 멤버데이터_비정상_저장_로그인_아이디() throws Exception {
        //given



        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(MEMBER_SIGNUP_URL)
                        .content(gson.toJson(memberSignUpRequestDto("dionisos198","password",
                                Sex.M,LocalDate.of(2023,1,1),"dionisos198","이진우","dionisos198naver.com")))
                        .contentType(MediaType.APPLICATION_JSON)
        );


        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("[email](은)는 이메일 형식에 맞지 않습니다 입력된 값: [dionisos198naver.com]"));


    }




    @Test
    @DisplayName("비정상적인 멤버 데이터 저장 상황-남과 여 둘중에 하나가 아닐 때 ")
    public void 멤버데이터_비정상_저장_ENUM_남자_여자() throws Exception {
        //given



        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(MEMBER_SIGNUP_URL)
                        .content(gson.toJson(memberSignUpRequestDto("dionisos198","password",
                                null,LocalDate.of(2023,1,1),"dionisos198","이진우","dionisos198@naver.com")))
                        .contentType(MediaType.APPLICATION_JSON)
        );


        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.message").value("[sex](은)는 남자와 여자 둘중에 하나를 선택해주세요 입력된 값: [null]"));


    }



}

