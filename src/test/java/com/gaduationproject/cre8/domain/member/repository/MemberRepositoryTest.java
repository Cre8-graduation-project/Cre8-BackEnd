package com.gaduationproject.cre8.domain.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gaduationproject.cre8.domain.config.QueryDSLConfig;
import com.gaduationproject.cre8.domain.member.editor.MemberEditor;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.type.Sex;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY) //Any : In memory db ,None: 실제 사용하는 datasource 사용
    @Import({QueryDSLConfig.class})
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
        assertThrows(DataIntegrityViolationException.class,()->{
            Member findMember2 = memberRepository.save(member1);
        });



    }

    @Test
    @DisplayName("회원이 처음 생성시 Profile 이 자동으로 생성되는지 확인 ")
    public void defaultProfile(){
        //given
        Member member = memberRepository.save(Member.builder()
                .email("dionisos198@naver.com")
                .name("jinu")
                .sex(Sex.W)
                .birthDay(LocalDate.now())
                .loginId("dionisos198")
                .password("hi")
                .nickName("jiji")
                .build());

        //when


        //then

        assertThat(member.getPersonalLink()).isEqualTo(null);
        assertThat(member.getPersonalStatement()).isEqualTo(null);
        assertThat(member.getTwitterLink()).isEqualTo(null);
        assertThat(member.getYoutubeLink()).isEqualTo(null);

    }

    @Test
    @DisplayName("일반 수정 테스트")
    public void Profile_수정_테스트(){

        //given
        Member member = memberRepository.save(Member.builder()
                .email("dionisos198@naver.com")
                .name("jinu")
                .sex(Sex.W)
                .birthDay(LocalDate.now())
                .loginId("dionisos198")
                .password("hi")
                .nickName("jiji")
                .build());

        //when
        MemberEditor.MemberEditorBuilder memberEditorBuilder = member.toEditor();
        MemberEditor memberEditor = memberEditorBuilder.accessUrl("www.image.com")
                .youtubeLink("www.youtube.com")
                        .personalLink("www.personalLink.com")
                                .twitterLink("www.twitter.com")
                                        .personalStatement("잘부탁드립니다")
                                                .nickName("jeje").build();
        member.edit(memberEditor);



        //then

        assertThat(member.getYoutubeLink()).isEqualTo("www.youtube.com");
        assertThat(member.getAccessUrl()).isEqualTo("www.image.com");
        assertThat(member.getPersonalLink()).isEqualTo("www.personalLink.com");
        assertThat(member.getTwitterLink()).isEqualTo("www.twitter.com");
        assertThat(member.getPersonalStatement()).isEqualTo("잘부탁드립니다");
        assertThat(member.getNickName()).isEqualTo("jeje");

    }




}