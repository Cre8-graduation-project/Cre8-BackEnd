package com.gaduationproject.cre8.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.type.Sex;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) //Any : In memory db ,None: 실제 사용하는 datasource 사용
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원이 회원가입 시도- 정상")
    public void saveMember(){
        Member member = Member.builder()
                .nickName("dionisos198")
                .password("password")
                .sex(Sex.M)
                .birthDay(LocalDate.of(2023,3,1))
                .email("dionisos198@naver.com")
                .name("jinu Yi")
                .build();

        Member findMember = memberRepository.save(member);

        assertThat(findMember).isNotNull();
        assertThat(findMember.getName()).isEqualTo("jinu Yi");
    }



}