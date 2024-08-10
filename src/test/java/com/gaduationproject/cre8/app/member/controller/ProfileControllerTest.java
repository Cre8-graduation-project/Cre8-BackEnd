package com.gaduationproject.cre8.app.member.controller;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gaduationproject.cre8.app.member.dto.ProfileWithUserInfoEditRequestDto;
import com.gaduationproject.cre8.app.member.dto.ProfileWithUserInfoResponseDto;
import com.gaduationproject.cre8.app.member.service.ProfileService;
import com.gaduationproject.cre8.common.LocalDateDeserializer;
import com.gaduationproject.cre8.common.LocalDateSerializer;
import com.gaduationproject.cre8.common.WithMockCustomUser;
import com.gaduationproject.cre8.common.response.error.RestExceptionHandler;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@WebMvcTest(com.gaduationproject.cre8.app.member.controller.ProfileControllerTest.class)
//@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {


    @InjectMocks
    private ProfileController profileController;

    @Mock
    private ProfileService profileService;

    @Mock
    private MemberRepository memberRepository;


    private MockMvc mockMvc;
    private Gson gson;

    private static final String PROFILE_URL = "/api/v1/profiles";

    @BeforeEach
    public void init() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());

        gson = gsonBuilder.setPrettyPrinting().create();

        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setControllerAdvice(new RestExceptionHandler())
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .build();


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

    private ProfileWithUserInfoEditRequestDto profileEditRequestDto(final String youtubeLink,final String twitterLink,final String personalLink
            ,final String personalStatement,final String userNickName){


        return ProfileWithUserInfoEditRequestDto.builder()
                .personalLink(personalLink)
                .twitterLink(twitterLink)
                .youtubeLink(youtubeLink)
                .personalStatement(personalStatement)
                .userNickName(userNickName)
                .build();

    }

    private ProfileWithUserInfoResponseDto defaultProfile(){
        return ProfileWithUserInfoResponseDto.of(makeDefaultMember());

    }


    @Test
    @DisplayName("프로필 아이디 기반 조회")
    public void 멤버_프로필_아이디_기반_조회() throws Exception {
        //given

        ProfileWithUserInfoResponseDto defaultProfileResposne = defaultProfile();
        given(profileService.showProfile(eq(1L))).willReturn(defaultProfileResposne);

        //when
        final ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/{memberId}/profile",1L)
                )
                .andDo(print());


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data.youtubeLink").value(nullValue()));

    }



    @Test
    @DisplayName("프로필 수정")
    @WithMockCustomUser(loginId = "dionisos198", name = "이진우")
    public void 멤버_프로필_수정() throws Exception {
        //given

        System.out.println();
        doNothing().when(profileService).changeMemberProfile(eq("dionisos198"), any(
                ProfileWithUserInfoEditRequestDto.class));

        ProfileWithUserInfoEditRequestDto profileWithUserInfoEditRequestDto = ProfileWithUserInfoEditRequestDto
                .builder()
                .userNickName("빠빠")
                .youtubeLink("www.youtube.com")
                .build();


        //when
        final ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.put(PROFILE_URL)
                           //     .content(gson.toJson(profileWithUserInfoEditRequestDto))
                                .param("userNickName",profileWithUserInfoEditRequestDto.getUserNickName())
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print());


        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());

    }



}