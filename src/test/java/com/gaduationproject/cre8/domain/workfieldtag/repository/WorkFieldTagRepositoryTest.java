package com.gaduationproject.cre8.domain.workfieldtag.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gaduationproject.cre8.domain.config.QueryDSLConfig;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
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
class WorkFieldTagRepositoryTest {

    @Autowired
    WorkFieldTagRepository workFieldTagRepository;


    @Test
    @DisplayName("이름 기반 조회")
    public void 이름_기반_조회(){

        //given
        workFieldTagRepository.save(WorkFieldTag.builder().name("헤헤").build());
        workFieldTagRepository.save(WorkFieldTag.builder().name("페페").build());


        //when
        Boolean first  = workFieldTagRepository.existsByName("페페");
        Boolean second  = workFieldTagRepository.existsByName("헤헤");
        Boolean third  = workFieldTagRepository.existsByName("ㅎㅎ");

        //then
        assertThat(first).isTrue();
        assertThat(second).isTrue();
        assertThat(third).isFalse();
    }

}