package com.gaduationproject.cre8.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.domain.member.type.Sex;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) //Any : In memory db ,None: 실제 사용하는 datasource 사용
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원이 회원가입 시도- 정상")
    public void saveMember(){

        //given
        Member member = Member.builder()
                .loginId("dionisos198")
                .nickName("dionisos198")
                .password("password")
                .sex(Sex.M)
                .birthDay(LocalDate.of(2023,3,1))
                .email("dionisos198@naver.com")
                .name("jinu Yi")
                .profile(Profile.builder()
                        .twitterLink(null)
                        .personalLink(null)
                        .youtubeLink(null)
                        .personalStatement(null)
                        .build())
                .build();

        //when
        Member findMember = memberRepository.save(member);

        //then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getName()).isEqualTo("jinu Yi");
    }

    @Test
    @DisplayName("회원이 회원가입 시도시 아이디 (이메일 ) 제약 조건에 위배")
    public void 닉네임_unique_제약조건위배(){
        //given
        Member member = Member.builder()
                .nickName("dionisos198")
                .loginId("dionisos198")
                .password("password")
                .sex(Sex.M)
                .birthDay(LocalDate.of(2023,3,1))
                .email("dionisos198@naver.com")
                .name("jinu Yi")
                .profile(Profile.builder()
                        .twitterLink(null)
                        .personalLink(null)
                        .youtubeLink(null)
                        .personalStatement(null)
                        .build())
                .build();

        Member findMember = memberRepository.save(member);

        Member member1 = Member.builder()
                        .nickName("dionisos198")
                                .password("password1")
                                        .sex(Sex.M)
                                                .birthDay(LocalDate.of(2023,3,1))
                                                        .email("dionisos1998@naver.com")
                                                                .name("jinu Yi")
                                                                        .build();

        //when , then
        org.junit.jupiter.api.Assertions.assertThrows(DataIntegrityViolationException.class,()->{
            Member findMember2 = memberRepository.save(member1);
        });



    }



}