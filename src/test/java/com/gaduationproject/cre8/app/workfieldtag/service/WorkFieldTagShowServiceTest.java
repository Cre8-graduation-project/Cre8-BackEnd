package com.gaduationproject.cre8.app.workfieldtag.service;

import static org.junit.jupiter.api.Assertions.*;

import com.gaduationproject.cre8.app.member.service.ProfileService;
import com.gaduationproject.cre8.app.workfieldtag.dto.response.WorkFieldChildTagWithSubCategoryNameResponseDto;
import com.gaduationproject.cre8.app.workfieldtag.dto.response.WorkFieldSubCategoryResponseDto;
import com.gaduationproject.cre8.app.workfieldtag.dto.response.WorkFieldTagResponseDto;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldSubCategory;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldSubCategoryRepository;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldTagRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkFieldTagShowServiceTest {

    @InjectMocks
    WorkFieldTagShowService workFieldTagShowService;
    @Mock
    WorkFieldTagRepository workFieldTagRepository;

    @Mock
    WorkFieldSubCategoryRepository workFieldSubCategoryRepository;


    private Member getDefaultMember(){
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

        return member;
    }


    @Test
    @DisplayName("모든 작업 분야 태그 확인")
    public void 작업_분야_태그_확인(){

        //given
        WorkFieldTag workFieldTag1 = WorkFieldTag.builder().name("작업분야1").build();
        WorkFieldTag workFieldTag2 = WorkFieldTag.builder().name("작업분야1").build();
        when(workFieldTagRepository.findAll()).thenReturn(List.of(workFieldTag1,workFieldTag2));

        //when
        List<WorkFieldTagResponseDto> workFieldTagResponseDtoList = workFieldTagShowService.showAllWorkFieldTag();


        //then
        assertThat(workFieldTagResponseDtoList).size().isEqualTo(2);
        assertThat(workFieldTagResponseDtoList.get(0).getName()).isEqualTo("작업분야1");
        assertThat(workFieldTagResponseDtoList.get(1).getName()).isEqualTo("작업분야1");


    }

    @Test
    @DisplayName("모든 작업 분야 아이디 기반 서브카테고리 확인")
    public void 모든_작업_분야_아이디_기반_서브카테고리_확인(){

        //given
        WorkFieldTag workFieldTag1 = WorkFieldTag.builder().name("작업분야1").build();
        ReflectionTestUtils.setField(workFieldTag1,"id",1L);
        WorkFieldSubCategory workFieldSubCategory1 = WorkFieldSubCategory.builder()
                        .workFieldTag(workFieldTag1)
                                .name("서브카테고리1")
                                        .build();
        WorkFieldSubCategory workFieldSubCategory2 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag1)
                .name("서브카테고리2")
                .build();
        when(workFieldSubCategoryRepository.findByWorkFieldTagId(eq(1L))).thenReturn(List.of(workFieldSubCategory1,workFieldSubCategory2));

        //when
        List<WorkFieldSubCategoryResponseDto> workFieldSubCategoryResponseDtoList = workFieldTagShowService.showAllWorkFieldSubCategoryByWorkFieldId(
                1L);

        //then
        assertThat(workFieldSubCategoryResponseDtoList.size()).isEqualTo(2);
        assertThat(workFieldSubCategoryResponseDtoList.get(0).getWorkFieldSubCategoryName()).isEqualTo("서브카테고리1");
        assertThat(workFieldSubCategoryResponseDtoList.get(1).getWorkFieldSubCategoryName()).isEqualTo("서브카테고리2");



    }

    @Test
    @DisplayName("모든 작업 분야 아이디 기반 서브카테고리 , 하위 태그확인")
    public void 모든_작업_분야_아이디_기반_서브카테고리_하위태그_확인(){

        //given
        WorkFieldTag workFieldTag1 = WorkFieldTag.builder().name("작업분야1").build();
        ReflectionTestUtils.setField(workFieldTag1,"id",1L);
        WorkFieldSubCategory workFieldSubCategory1 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag1)
                .name("서브카테고리1")
                .build();
        WorkFieldSubCategory workFieldSubCategory2 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag1)
                .name("서브카테고리2")
                .build();
        workFieldSubCategory1.addWorkFieldChildTag("하위태그1");
        workFieldSubCategory1.addWorkFieldChildTag("하위태그2");
        workFieldSubCategory2.addWorkFieldChildTag("하위태그3");
        when(workFieldSubCategoryRepository.findByWorkFieldTagWithFetchWorkFieldChildTagList(eq(1L))).thenReturn(List.of(workFieldSubCategory1,workFieldSubCategory2));

        //when
        List<WorkFieldChildTagWithSubCategoryNameResponseDto> workFieldChildTagWithSubCategoryNameResponseDtos = workFieldTagShowService.showAllChildTagByWorkFieldId(
                1L);

        //then
        assertThat(workFieldChildTagWithSubCategoryNameResponseDtos.size()).isEqualTo(2);
        assertThat(workFieldChildTagWithSubCategoryNameResponseDtos.get(0).getSubCategoryName()).isEqualTo("서브카테고리1");
        assertThat(workFieldChildTagWithSubCategoryNameResponseDtos.get(1).getSubCategoryName()).isEqualTo("서브카테고리2");
        assertThat(workFieldChildTagWithSubCategoryNameResponseDtos.get(0).getWorkFieldChildTagResponseDtoList().size()).isEqualTo(2);
        assertThat(workFieldChildTagWithSubCategoryNameResponseDtos.get(1).getWorkFieldChildTagResponseDtoList().size()).isEqualTo(1);




    }

}