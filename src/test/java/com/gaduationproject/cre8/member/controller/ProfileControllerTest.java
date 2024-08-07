package com.gaduationproject.cre8.member.controller;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gaduationproject.cre8.api.member.controller.ProfileController;
import com.gaduationproject.cre8.common.LocalDateDeserializer;
import com.gaduationproject.cre8.common.LocalDateSerializer;
import com.gaduationproject.cre8.common.WithMockCustomUser;
import com.gaduationproject.cre8.common.response.error.RestExceptionHandler;
import com.gaduationproject.cre8.api.member.dto.ProfileWithUserInfoEditRequestDto;
import com.gaduationproject.cre8.api.member.dto.ProfileWithUserInfoResponseDto;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.api.member.service.ProfileService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(ProfileControllerTest.class)
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

    private ProfileWithUserInfoEditRequestDto profileEditRequestDto(final String youtubeLink,final String twitterLink,final String personalLink
    ,final String personalStatement){
        return ProfileWithUserInfoEditRequestDto.builder()
                .personalLink(personalLink)
                .twitterLink(twitterLink)
                .youtubeLink(youtubeLink)
                .personalStatement(personalStatement)
                .build();

    }

    private ProfileWithUserInfoResponseDto defaultProfile(){
        return ProfileWithUserInfoResponseDto.builder()
                .profile(Profile.builder().youtubeLink(null).twitterLink(null).personalLink(null).personalStatement(null).build())
                .build();

    }

    private ProfileWithUserInfoResponseDto changedProfile(final String youtubeLink,final String twitterLink,final String personalLink
            ,final String personalStatement){
        return ProfileWithUserInfoResponseDto.builder()
                .profile(Profile.builder().youtubeLink(youtubeLink).twitterLink(twitterLink).personalLink(personalLink).personalStatement(personalStatement).build())
                .build();

    }


    @Test
    @DisplayName("프로필 정상 조회 - 초기")
    @WithMockCustomUser(loginId = "dionisos198", name = "이진우")
    public void 멤버_프로필_초기_정상_조회() throws Exception {
        //given

        ProfileWithUserInfoResponseDto defaultProfileResposne = defaultProfile();
        given(profileService.showMyProfile(eq("dionisos198"))).willReturn(defaultProfileResposne);

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(PROFILE_URL)
        )
                .andDo(print());


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data.youtubeLink").value(nullValue()));

    }

    @Test
    @DisplayName("프로필 정상 조회 - 수정 후")
    @WithMockCustomUser(loginId = "dionisos198", name = "이진우")
    public void 멤버_프로필_수정후_정상_조회() throws Exception {
        //given

        ProfileWithUserInfoResponseDto changedProfileResposne = changedProfile("www.youbue.com",null,null,"사랑");
        given(profileService.showMyProfile(eq("dionisos198"))).willReturn(changedProfileResposne);

        //when
        final ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.get(PROFILE_URL)
                )
                .andDo(print());


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data.youtubeLink").value("www.youbue.com"))
                .andExpect(jsonPath("$.data.personalStatement").value("사랑"));

    }

    @Test
    @DisplayName("프로필 수정")
    @WithMockCustomUser(loginId = "dionisos198", name = "이진우")
    public void 멤버_프로필_수정() throws Exception {
        //given

        doNothing().when(profileService).changeMemberProfile(eq("dionisos198"), any(
                ProfileWithUserInfoEditRequestDto.class));


        //when
        final ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.put(PROFILE_URL)
                                .content(gson.toJson(profileEditRequestDto("www.youtube.com"
                                ,null,null,"난 천재야")))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print());


        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());

    }



}