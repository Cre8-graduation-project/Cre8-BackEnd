package com.gaduationproject.cre8.domain.portfolio.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gaduationproject.cre8.domain.config.QueryDSLConfig;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioImage;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
class PortfolioImageRepositoryTest {

    @Autowired
    PortfolioImageRepository portfolioImageRepository;

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    MemberRepository memberRepository;

    public Portfolio savedPortfolioWithPortfolioImage(){

        Member member = memberRepository.save(Member.builder()
                .nickName("dionisos198")
                .email("dionisos198@naver.com")
                .name("이진우")
                .loginId("dionisos198")
                .password("password")
                .sex(Sex.M)
                .birthDay(LocalDate.now())
                .build());

        Portfolio portfolio = Portfolio.builder().member(member).build();
        portfolioRepository.save(portfolio);

        PortfolioImage portfolioImage = PortfolioImage.builder()
                .portfolio(portfolio)
                .accessUrl("www.access1.com")
                .originalName("access")
                .build();

        PortfolioImage portfolioImage2 = PortfolioImage.builder()
                .portfolio(portfolio)
                .accessUrl("www.access2.com")
                .originalName("access2")
                .build();

        PortfolioImage portfolioImage3 = PortfolioImage.builder()
                .portfolio(portfolio)
                .accessUrl("www.access3.com")
                .originalName("access3")
                .build();

        portfolioImageRepository.save(portfolioImage);
        portfolioImageRepository.save(portfolioImage2);
        portfolioImageRepository.save(portfolioImage3);

        return portfolio;

    }

    public Portfolio savedPortfolioWithOutPortfolioImage(){

        Member member = memberRepository.save(Member.builder()
                .nickName("dionisos198")
                .email("dionisos198@naver.com")
                .name("이진우")
                .loginId("dionisos198")
                .password("password")
                .sex(Sex.M)
                .birthDay(LocalDate.now())
                .build());

        Portfolio portfolio = Portfolio.builder().member(member).build();
        portfolioRepository.save(portfolio);

        return portfolio;

    }


    @Test
    @DisplayName("포트폴리오 이미지 조회 by 포트폴리오")
    public void 포트폴리오_이미지_조회_BY_포트폴리오(){

        //given
        Portfolio portfolio = savedPortfolioWithPortfolioImage();

        //when
        List<PortfolioImage> portfolioImageList = portfolioImageRepository.findByPortfolio(portfolio);

        //then
        assertThat(portfolioImageList).size().isEqualTo(3);
        assertThat(portfolioImageList.get(0).getAccessUrl()).isEqualTo("www.access1.com");
    }

    @Test
    @DisplayName("포트폴리오 이미지 조회 by 포트폴리오 이미지 저장 없을 때 ")
    public void 포트폴리오_이미지_조회_BY_포트폴리오_이미지_저장X(){

        //given
        Portfolio portfolio = savedPortfolioWithOutPortfolioImage();

        //when
        List<PortfolioImage> portfolioImageList = portfolioImageRepository.findByPortfolio(portfolio);

        //then
        assertThat(portfolioImageList).size().isEqualTo(0);

    }

}