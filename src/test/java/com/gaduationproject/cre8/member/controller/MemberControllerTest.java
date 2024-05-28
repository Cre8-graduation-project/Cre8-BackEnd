package com.gaduationproject.cre8.member.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.fasterxml.jackson.core.JsonParseException;
import com.gaduationproject.cre8.common.LocalDateDeserializer;
import com.gaduationproject.cre8.common.LocalDateSerializer;
import com.gaduationproject.cre8.common.response.error.RestExceptionHandler;
import com.gaduationproject.cre8.member.dto.MemberSignUpRequestDto;
import com.gaduationproject.cre8.member.service.MemberSignUpService;
import com.gaduationproject.cre8.member.type.Sex;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ResponseBody;

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
             final String email, final String name){
        return MemberSignUpRequestDto.builder()
                .nickName(nickName)
                .password(password)
                .sex(sex)
                .birthDay(birthDay)
                .email(email)
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
                                Sex.M,LocalDate.of(2023,1,1),"dionisos198@naver.com","이진우")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isCreated());

    }

    @Test
    @DisplayName("비정상적인 멤버 데이터 저장 상황-이메일")
    public void 멤버데이터_비정상_저장_이메일() throws Exception {
        //given



        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(MEMBER_SIGNUP_URL)
                        .content(gson.toJson(memberSignUpRequestDto("dionisos198","password",
                                Sex.M,LocalDate.of(2023,1,1),"dionisos198naver.com","이진우")))
                        .contentType(MediaType.APPLICATION_JSON)
        );


        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("[email](은)는 이메일 형식이 올바르지 않습니다 입력된 값: [dionisos198naver.com]"));


    }

    @Test
    @DisplayName("비정상적인 멤버 데이터 저장 상황-남과 여 둘중에 하나가 아닐 때 ")
    public void 멤버데이터_비정상_저장_ENUM_남자_여자() throws Exception {
        //given



        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(MEMBER_SIGNUP_URL)
                        .content(gson.toJson(memberSignUpRequestDto("dionisos198","password",
                                null,LocalDate.of(2023,1,1),"dionisos198@naver.com","이진우")))
                        .contentType(MediaType.APPLICATION_JSON)
        );


        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("[sex](은)는 M 또는 W 둘 중 하나만 입력이 가능합니다 입력된 값: [null]"));


    }



}

