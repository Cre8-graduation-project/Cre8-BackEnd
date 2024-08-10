package com.gaduationproject.cre8.domain.workfieldtag.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gaduationproject.cre8.domain.config.QueryDSLConfig;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldSubCategory;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY) //Any : In memory db ,None: 실제 사용하는 datasource 사용
@Import({QueryDSLConfig.class})
class WorkFieldSubCategoryRepositoryTest {


    @Autowired
    WorkFieldSubCategoryRepository workFieldSubCategoryRepository;

    @Autowired
    WorkFieldTagRepository workFieldTagRepository;


    @Autowired
    WorkFieldChildTagRepository workFieldChildTagRepository;


    @Test
    @DisplayName("이름 작업분야 기반 조회")
    public void 이름_WORK_FIELD_조회(){

        //given
        WorkFieldTag workFieldTag1 = workFieldTagRepository.save(WorkFieldTag.builder().name("편집").build());
        WorkFieldTag workFieldTag2 = workFieldTagRepository.save(WorkFieldTag.builder().name("자막").build());
        WorkFieldSubCategory workFieldSubCategory1 = workFieldSubCategoryRepository.save
                (WorkFieldSubCategory.builder().workFieldTag(workFieldTag1).name("작업분야1").build());
        WorkFieldSubCategory workFieldSubCategory2 = workFieldSubCategoryRepository.save
                (WorkFieldSubCategory.builder().workFieldTag(workFieldTag1).name("작업분야2").build());
        WorkFieldSubCategory workFieldSubCategory3 = workFieldSubCategoryRepository.save
                (WorkFieldSubCategory.builder().workFieldTag(workFieldTag2).name("작업분야3").build());
        WorkFieldSubCategory workFieldSubCategory4 = workFieldSubCategoryRepository.save
                (WorkFieldSubCategory.builder().workFieldTag(workFieldTag2).name("작업분야4").build());
        WorkFieldChildTag workFieldChildTag1 = workFieldChildTagRepository.save
                (WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1).name("자식태그1").build());
        WorkFieldChildTag workFieldChildTag2 = workFieldChildTagRepository.save
                (WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1).name("자식태그2").build());
        WorkFieldChildTag workFieldChildTag3 = workFieldChildTagRepository.save
                (WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1).name("자식태그3").build());
        WorkFieldChildTag workFieldChildTag4 = workFieldChildTagRepository.save
                (WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1).name("자식태그4").build());
        WorkFieldChildTag workFieldChildTag5 = workFieldChildTagRepository.save
                (WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1).name("자식태그5").build());
        WorkFieldChildTag workFieldChildTag6 = workFieldChildTagRepository.save
                (WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1).name("자식태그6").build());
        WorkFieldChildTag workFieldChildTag7 = workFieldChildTagRepository.save
                (WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1).name("자식태그7").build());
        WorkFieldChildTag workFieldChildTag8 = workFieldChildTagRepository.save
                (WorkFieldChildTag.builder().workFieldSubCategory(workFieldSubCategory1).name("자식태그8").build());


        //when
        Boolean first = workFieldSubCategoryRepository.existsByNameAndWorkFieldTag("작업분야1",workFieldTag1);
        Boolean second = workFieldSubCategoryRepository.existsByNameAndWorkFieldTag("작업분야2",workFieldTag1);
        Boolean third = workFieldSubCategoryRepository.existsByNameAndWorkFieldTag("작업분야3",workFieldTag1);

        //then
        assertThat(first).isTrue();
        assertThat(second).isTrue();
        assertThat(third).isFalse();
    }


    @Test
    @DisplayName("Workfield id 기반 조회-페치조인")
    public void WORK_FIELD_ID_기반_조회_페치조인(){

        //given
        WorkFieldTag workFieldTag1 = workFieldTagRepository.save(WorkFieldTag.builder().name("작업분야1").build());
        WorkFieldTag workFieldTag2 = workFieldTagRepository.save(WorkFieldTag.builder().name("작업분야2").build());

        workFieldTag1.addWorkFieldSubCategory("하위카테고리1");
        workFieldTag1.addWorkFieldSubCategory("하위카테고리2");
        workFieldTag2.addWorkFieldSubCategory("하위카테고리3");
        workFieldTag2.addWorkFieldSubCategory("하위카테고리4");

        workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldTag1.getId()).get(0).addWorkFieldChildTag("자식분야1");
        workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldTag1.getId()).get(0).addWorkFieldChildTag("자식분야2");
        workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldTag1.getId()).get(1).addWorkFieldChildTag("자식분야3");
        workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldTag1.getId()).get(1).addWorkFieldChildTag("자식분야4");
        workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldTag2.getId()).get(0).addWorkFieldChildTag("자식분야5");
        workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldTag2.getId()).get(0).addWorkFieldChildTag("자식분야6");
        workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldTag2.getId()).get(1).addWorkFieldChildTag("자식분야7");






        //when
        List<WorkFieldSubCategory> byWorkFieldTagWithFetchWorkFieldChildTagList = workFieldSubCategoryRepository.findByWorkFieldTagWithFetchWorkFieldChildTagList(
                workFieldTag1.getId());

        List<WorkFieldSubCategory> byWorkFieldTagWithFetchWorkFieldChildTagList1 = workFieldSubCategoryRepository.findByWorkFieldTagWithFetchWorkFieldChildTagList(
                workFieldTag2.getId());

        //then
        assertThat(byWorkFieldTagWithFetchWorkFieldChildTagList).size().isEqualTo(2);
        assertThat(byWorkFieldTagWithFetchWorkFieldChildTagList1).size().isEqualTo(2);
        assertThat(byWorkFieldTagWithFetchWorkFieldChildTagList.get(0).getWorkFieldChildTagList()).size().isEqualTo(2);
        assertThat(byWorkFieldTagWithFetchWorkFieldChildTagList.get(1).getWorkFieldChildTagList()).size().isEqualTo(2);
        assertThat(byWorkFieldTagWithFetchWorkFieldChildTagList1.get(0).getWorkFieldChildTagList()).size().isEqualTo(2);
        assertThat(byWorkFieldTagWithFetchWorkFieldChildTagList1.get(1).getWorkFieldChildTagList()).size().isEqualTo(1);

    }

}