package com.gaduationproject.cre8.member.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.entity.Profile;
import com.gaduationproject.cre8.member.type.Sex;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) //Any : In memory db ,None: 실제 사용하는 datasource 사용
class ProfileRepositoryTest {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("회원이 처음 생성시 Profile 이 자동으로 생성되는지 확인 ")
    public void defaultProfile(){
        //given
        Member member = memberRepository.save(Member.builder()
                .profile(makeEmptyProfile())
                .email("dionisos198@naver.com")
                .name("jinu")
                .sex(Sex.W)
                .birthDay(LocalDate.now())
                .loginId("dionisos198")
                .password("hi")
                .nickName("jiji")
                .build());

        //when
        Profile profile = member.getProfile();

        //then

        assertThat(profile.getPersonalLink()).isEqualTo(null);
        assertThat(profile.getPersonalStatement()).isEqualTo(null);
        assertThat(profile.getTwitterLink()).isEqualTo(null);
        assertThat(profile.getYoutubeLink()).isEqualTo(null);

    }

    @Test
    @DisplayName("회원 저장시 자동으로 ProfileRepository에 저장되는지 확인 - cascadeType all")
    public void 회원생성_자동_Profile_저장(){
        //given, when
        Member member = memberRepository.save(Member.builder()
                .profile(makeEmptyProfile())
                .email("dionisos198@naver.com")
                .name("jinu")
                .sex(Sex.W)
                .birthDay(LocalDate.now())
                .loginId("dionisos198")
                .password("hi")
                .nickName("jiji")
                .build());

        //then

        assertThat(profileRepository.findAll().size()).isEqualTo(1);

    }

    @Test
    @DisplayName("일반 저장 테스트")
    public void Profile_일반_저장_테스트(){
        //given
        Profile profile = Profile.builder()
                .twitterLink("www.naver.com")
                .youtubeLink("www.youtube.com")
                .personalLink("www.personlink.com")
                .personalStatement("안녕하세요 이 시대의 천재")
                .build();

        //when
        Profile findProfile = profileRepository.save(profile);

        //then

        assertThat(findProfile.getYoutubeLink()).isEqualTo("www.youtube.com");
        assertThat(findProfile.getPersonalStatement()).isEqualTo("안녕하세요 이 시대의 천재");

    }

    private Profile makeEmptyProfile(){
        return Profile.builder()
                .personalLink(null)
                .twitterLink(null)
                .personalStatement(null)
                .youtubeLink(null)
                .build();
    }


}
