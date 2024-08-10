package com.gaduationproject.cre8.domain.workfieldtag.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gaduationproject.cre8.domain.config.QueryDSLConfig;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldSubCategory;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import org.assertj.core.api.Assertions;
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
class WorkFieldChildTagRepositoryTest {

    @Autowired
    WorkFieldTagRepository workFieldTagRepository;

    @Autowired
    WorkFieldChildTagRepository workFieldChildTagRepository;

    @Autowired
    WorkFieldSubCategoryRepository workFieldSubCategoryRepository;

    @Test
    @DisplayName("이름,서브카테고리 기반 존재확인")
    public void 이름_서브카테고리_기반_EXIST(){
        //given
        WorkFieldTag workFieldTag1 = workFieldTagRepository.save(WorkFieldTag.builder().name("작업분야1").build());
        WorkFieldTag workFieldTag2 = workFieldTagRepository.save(WorkFieldTag.builder().name("작업분야2").build());

        workFieldTag1.addWorkFieldSubCategory("하위카테고리1");
        workFieldTag1.addWorkFieldSubCategory("하위카테고리2");
        workFieldTag2.addWorkFieldSubCategory("하위카테고리3");
        workFieldTag2.addWorkFieldSubCategory("하위카테고리4");

        WorkFieldSubCategory workFieldSubCategory1 = workFieldTag1.getWorkFieldSubCategoryList()
                .get(0);
        WorkFieldSubCategory workFieldSubCategory2 = workFieldTag1.getWorkFieldSubCategoryList()
                .get(1);
        WorkFieldSubCategory workFieldSubCategory3 = workFieldTag2.getWorkFieldSubCategoryList()
                .get(0);
        WorkFieldSubCategory workFieldSubCategory4 = workFieldTag2.getWorkFieldSubCategoryList()
                .get(1);


        workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldTag1.getId()).get(0).addWorkFieldChildTag("자식분야1");
        workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldTag1.getId()).get(0).addWorkFieldChildTag("자식분야2");
        workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldTag1.getId()).get(1).addWorkFieldChildTag("자식분야3");
        workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldTag1.getId()).get(1).addWorkFieldChildTag("자식분야4");
        workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldTag2.getId()).get(0).addWorkFieldChildTag("자식분야5");
        workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldTag2.getId()).get(0).addWorkFieldChildTag("자식분야6");
        workFieldSubCategoryRepository.findByWorkFieldTagId(workFieldTag2.getId()).get(1).addWorkFieldChildTag("자식분야7");

        //when
        Boolean first = workFieldChildTagRepository.existsByNameAndWorkFieldSubCategory("자식분야1",workFieldSubCategory1);
        Boolean second = workFieldChildTagRepository.existsByNameAndWorkFieldSubCategory("자식분야2",workFieldSubCategory1);
        Boolean third = workFieldChildTagRepository.existsByNameAndWorkFieldSubCategory("자식분야3",workFieldSubCategory1);



        //then
        assertThat(first).isTrue();
        assertThat(second).isTrue();
        assertThat(third).isFalse();

    }

}