package com.gaduationproject.cre8.app.workfieldtag.controller;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gaduationproject.cre8.app.member.controller.MemberController;
import com.gaduationproject.cre8.app.member.dto.MemberSignUpRequestDto;
import com.gaduationproject.cre8.app.member.service.MemberSignUpService;
import com.gaduationproject.cre8.app.workfieldtag.dto.response.WorkFieldChildTagResponseDto;
import com.gaduationproject.cre8.app.workfieldtag.dto.response.WorkFieldChildTagWithSubCategoryNameResponseDto;
import com.gaduationproject.cre8.app.workfieldtag.dto.response.WorkFieldSubCategoryResponseDto;
import com.gaduationproject.cre8.app.workfieldtag.dto.response.WorkFieldTagResponseDto;
import com.gaduationproject.cre8.app.workfieldtag.service.WorkFieldTagShowService;
import com.gaduationproject.cre8.common.LocalDateDeserializer;
import com.gaduationproject.cre8.common.LocalDateSerializer;
import com.gaduationproject.cre8.common.response.error.RestExceptionHandler;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldSubCategory;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.jdbc.Work;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ExtendWith(MockitoExtension.class)
class WorkFieldTagShowControllerTest {


    @InjectMocks
    private WorkFieldTagShowController workFieldTagShowController;

    @Mock
    private WorkFieldTagShowService workFieldTagShowService;

    private MockMvc mockMvc;
    private Gson gson;

    private static final String TAG_URL = "/api/v1/tags";

    @BeforeEach
    public void init() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());

        gson = gsonBuilder.setPrettyPrinting().create();

        mockMvc = MockMvcBuilders.standaloneSetup(workFieldTagShowController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("작업분야 조회 확인")
    public void 작업분야_조회_확인() throws Exception {

        //given
        WorkFieldTagResponseDto workFieldTagResponseDto1 = WorkFieldTagResponseDto.of(
                WorkFieldTag.builder().name("작업분야1").build());
        WorkFieldTagResponseDto workFieldTagResponseDto2 = WorkFieldTagResponseDto.of(
                WorkFieldTag.builder().name("작업분야2").build());
        when(workFieldTagShowService.showAllWorkFieldTag()).thenReturn(List.of(workFieldTagResponseDto1,workFieldTagResponseDto2));

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(TAG_URL)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data.[0].name").value("작업분야1"));

    }



    @Test
    @DisplayName("작업분야 태그의 카테고리 확인")
    public void 작업분야_태그_카테고리_확인() throws Exception {

        //given
        WorkFieldTag workFieldTag1 = WorkFieldTag.builder().name("작업분야1").build();
        WorkFieldTag workFieldTag2 = WorkFieldTag.builder().name("작업분야2").build();
        ReflectionTestUtils.setField(workFieldTag1,"id",1L);
        ReflectionTestUtils.setField(workFieldTag1,"id",2L);

        WorkFieldSubCategoryResponseDto workFieldSubCategoryResponseDto1 = WorkFieldSubCategoryResponseDto.from(
                WorkFieldSubCategory.builder().workFieldTag(workFieldTag1).name("서브카테고리1").build());

        WorkFieldSubCategoryResponseDto workFieldSubCategoryResponseDto2 = WorkFieldSubCategoryResponseDto.from(
                WorkFieldSubCategory.builder().workFieldTag(workFieldTag1).name("서브카테고리2").build());

        when(workFieldTagShowService.showAllWorkFieldSubCategoryByWorkFieldId(1L)).thenReturn(List.of(workFieldSubCategoryResponseDto1,workFieldSubCategoryResponseDto2));

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(TAG_URL+"/subcategory/{workFieldIdTag}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data.[0].workFieldSubCategoryName").value("서브카테고리1"));

    }


    @Test
    @DisplayName("상위 작업 분야의 카테고리 이름, 하위 태그 조회")
    public void 작업분야_태그_카테고리_하위태그_확인() throws Exception {

        //given
        WorkFieldTag workFieldTag1 = WorkFieldTag.builder().name("작업분야1").build();
        WorkFieldTag workFieldTag2 = WorkFieldTag.builder().name("작업분야2").build();
        ReflectionTestUtils.setField(workFieldTag1,"id",1L);
        ReflectionTestUtils.setField(workFieldTag1,"id",2L);

        WorkFieldChildTag workFieldChildTag1 =
                WorkFieldChildTag.builder()
                        .workFieldSubCategory(WorkFieldSubCategory.builder().name("하위분야1").workFieldTag(workFieldTag1).build())
                        .name("자식태그1")
                        .build();
        WorkFieldChildTag workFieldChildTag2 =
                WorkFieldChildTag.builder()
                        .workFieldSubCategory(WorkFieldSubCategory.builder().name("하위분야1").workFieldTag(workFieldTag1).build())
                        .name("자식태그2")
                        .build();

        WorkFieldChildTag workFieldChildTag3 =
                WorkFieldChildTag.builder()
                        .workFieldSubCategory(WorkFieldSubCategory.builder().name("하위분야2").workFieldTag(workFieldTag1).build())
                        .name("자식태그3")
                        .build();


        WorkFieldChildTagResponseDto workFieldChildTagResponseDto1 = WorkFieldChildTagResponseDto.from(workFieldChildTag1);
        WorkFieldChildTagResponseDto workFieldChildTagResponseDto2 = WorkFieldChildTagResponseDto.from(workFieldChildTag2);
        WorkFieldChildTagResponseDto workFieldChildTagResponseDto3 = WorkFieldChildTagResponseDto.from(workFieldChildTag3);

        WorkFieldChildTagWithSubCategoryNameResponseDto workFieldChildTagWithSubCategoryNameResponseDto1 =
                WorkFieldChildTagWithSubCategoryNameResponseDto.of("하위분야1",List.of(workFieldChildTagResponseDto1,workFieldChildTagResponseDto2));

        WorkFieldChildTagWithSubCategoryNameResponseDto workFieldChildTagWithSubCategoryNameResponseDto2 =
                WorkFieldChildTagWithSubCategoryNameResponseDto.of("하위분야2",List.of(workFieldChildTagResponseDto3));

        when(workFieldTagShowService.showAllChildTagByWorkFieldId(1L)).thenReturn(List.of(workFieldChildTagWithSubCategoryNameResponseDto1,workFieldChildTagWithSubCategoryNameResponseDto2));

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(TAG_URL+"/child/{workFieldIdTag}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data.[0].subCategoryName").value("하위분야1"))
                .andExpect(jsonPath("$.data.[0].workFieldChildTagResponseDtoList.size()").value(2))
                .andExpect(jsonPath("$.data.[1].subCategoryName").value("하위분야2"))
                .andExpect(jsonPath("$.data.[1].workFieldChildTagResponseDtoList.size()").value(1));
    }


}