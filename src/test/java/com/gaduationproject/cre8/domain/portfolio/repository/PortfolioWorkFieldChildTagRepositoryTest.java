package com.gaduationproject.cre8.domain.portfolio.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gaduationproject.cre8.domain.config.QueryDSLConfig;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioImage;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioWorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldSubCategory;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldSubCategoryRepository;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldTagRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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
class PortfolioWorkFieldChildTagRepositoryTest {

    @Autowired
    PortfolioWorkFieldChildTagRepository portfolioWorkFieldChildTagRepository;


    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    WorkFieldTagRepository workFieldTagRepository;

    @Autowired
    WorkFieldSubCategoryRepository workFieldSubCategoryRepository;


    @Autowired
    WorkFieldChildTagRepository workFieldChildTagRepository;

    private Member getMember(){
        return memberRepository.save(Member.builder()
                .nickName("dionisos198")
                .email("dionisos198@naver.com")
                .name("이진우")
                .loginId("dionisos198")
                .password("password")
                .sex(Sex.M)
                .birthDay(LocalDate.now())
                .build());
    }


    @Test
    @DisplayName("포트폴리오 작업분야 child tag 리스트 - 포트폴리오 아이디 기반 조회")
    public void 포트폴리오_작업분야_CHILD_TAG_리스트_포트폴리오_아이디_기반_조회(){
        //given
        WorkFieldTag workFieldTag1 = workFieldTagRepository.save(WorkFieldTag.builder().name("작업분야1").build());
        WorkFieldTag workFieldTag2 = workFieldTagRepository.save(WorkFieldTag.builder().name("작업분야2").build());

        WorkFieldSubCategory workFieldSubCategory1 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag1)
                .name("하위카테고리1")
                .build();
        WorkFieldSubCategory workFieldSubCategory2 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag1)
                .name("하위카테고리2")
                .build();
        WorkFieldSubCategory workFieldSubCategory3 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag2)
                .name("하위카테고리3")
                .build();
        WorkFieldSubCategory workFieldSubCategory4 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag2)
                .name("하위카테고리4")
                .build();

        workFieldSubCategoryRepository.save(workFieldSubCategory1);
        workFieldSubCategoryRepository.save(workFieldSubCategory2);
        workFieldSubCategoryRepository.save(workFieldSubCategory3);
        workFieldSubCategoryRepository.save(workFieldSubCategory4);

        WorkFieldChildTag workFieldChildTag1 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory1)
                .name("자식분야1")
                .build();
        WorkFieldChildTag workFieldChildTag2 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory1)
                .name("자식분야2")
                .build();
        WorkFieldChildTag workFieldChildTag3 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory2)
                .name("자식분야3")
                .build();
        WorkFieldChildTag workFieldChildTag4 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory2)
                .name("자식분야4")
                .build();
        WorkFieldChildTag workFieldChildTag5 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory3)
                .name("자식분야5")
                .build();
        WorkFieldChildTag workFieldChildTag6 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory3)
                .name("자식분야6")
                .build();
        WorkFieldChildTag workFieldChildTag7 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory4)
                .name("자식분야7")
                .build();

        workFieldChildTagRepository.save(workFieldChildTag1);
        workFieldChildTagRepository.save(workFieldChildTag2);
        workFieldChildTagRepository.save(workFieldChildTag3);
        workFieldChildTagRepository.save(workFieldChildTag4);
        workFieldChildTagRepository.save(workFieldChildTag5);
        workFieldChildTagRepository.save(workFieldChildTag6);
        workFieldChildTagRepository.save(workFieldChildTag7);


        Member member = getMember();

        //portfolio 1 저장
        Portfolio portfolio1 = Portfolio.builder().member(member).build();
        portfolioRepository.save(portfolio1);

        portfolio1.changeWorkFieldTagAndDescription(workFieldTag1,"나는 코딩을 잘합니다");
        portfolio1.getPortfolioImageList().add(
                PortfolioImage.builder().portfolio(portfolio1).originalName("원래이름1").accessUrl("엑세스1").build());
        portfolio1.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio1).originalName("원래이름2").accessUrl("엑세스2").build());
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag1 =
                PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag1).portfolio(portfolio1).build();
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag2 =
                PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag2).portfolio(portfolio1).build();
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag3 =
                PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag3).portfolio(portfolio1).build();
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag4 =
                PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag4).portfolio(portfolio1).build();

        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag1);
        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag2);
        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag3);
        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag4);


        //portfolio 2 저장
        Portfolio portfolio2 = Portfolio.builder().member(member).build();
        portfolioRepository.save(portfolio2);

        portfolio2.changeWorkFieldTagAndDescription(workFieldTag2,"나는 코쿤을 잘합니다");
        portfolio2.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio2).originalName("원래이름3").accessUrl("엑세스3").build());
        portfolio2.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio2).originalName("원래이름4").accessUrl("엑세스4").build());

        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag5 =
                PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag5).portfolio(portfolio2).build();
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag6 =
                PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag6).portfolio(portfolio2).build();
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag7 =
                PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag7).portfolio(portfolio2).build();

        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag5);
        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag6);
        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag7);


        //when
        List<PortfolioWorkFieldChildTag> byPortfolioIdWithFetchWorkFieldChildTag1=
                portfolioWorkFieldChildTagRepository.findByPortfolioIdWithFetchWorkFieldChildTag(
                portfolio1.getId());

        List<PortfolioWorkFieldChildTag> byPortfolioIdWithFetchWorkFieldChildTag2 =
                portfolioWorkFieldChildTagRepository.findByPortfolioIdWithFetchWorkFieldChildTag(
                        portfolio2.getId());

        assertThat(byPortfolioIdWithFetchWorkFieldChildTag1.size()).isEqualTo(4);
        assertThat(byPortfolioIdWithFetchWorkFieldChildTag1.get(0).getWorkFieldChildTag()).isEqualTo(workFieldChildTag1);
        assertThat(byPortfolioIdWithFetchWorkFieldChildTag2.size()).isEqualTo(3);
        assertThat(byPortfolioIdWithFetchWorkFieldChildTag2.get(0).getWorkFieldChildTag()).isEqualTo(workFieldChildTag5);

    }


    @Test
    @DisplayName("포트폴리오 작업분야 child tag 삭제 by 포트폴리오")
    public void 포트폴리오_작업분야_CHILD_TAG_삭제_By_포트폴리오(){
        //given
        WorkFieldTag workFieldTag1 = workFieldTagRepository.save(WorkFieldTag.builder().name("작업분야1").build());
        WorkFieldTag workFieldTag2 = workFieldTagRepository.save(WorkFieldTag.builder().name("작업분야2").build());

        WorkFieldSubCategory workFieldSubCategory1 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag1)
                .name("하위카테고리1")
                .build();
        WorkFieldSubCategory workFieldSubCategory2 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag1)
                .name("하위카테고리2")
                .build();
        WorkFieldSubCategory workFieldSubCategory3 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag2)
                .name("하위카테고리3")
                .build();
        WorkFieldSubCategory workFieldSubCategory4 = WorkFieldSubCategory.builder()
                .workFieldTag(workFieldTag2)
                .name("하위카테고리4")
                .build();

        workFieldSubCategoryRepository.save(workFieldSubCategory1);
        workFieldSubCategoryRepository.save(workFieldSubCategory2);
        workFieldSubCategoryRepository.save(workFieldSubCategory3);
        workFieldSubCategoryRepository.save(workFieldSubCategory4);

        WorkFieldChildTag workFieldChildTag1 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory1)
                .name("자식분야1")
                .build();
        WorkFieldChildTag workFieldChildTag2 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory1)
                .name("자식분야2")
                .build();
        WorkFieldChildTag workFieldChildTag3 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory2)
                .name("자식분야3")
                .build();
        WorkFieldChildTag workFieldChildTag4 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory2)
                .name("자식분야4")
                .build();
        WorkFieldChildTag workFieldChildTag5 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory3)
                .name("자식분야5")
                .build();
        WorkFieldChildTag workFieldChildTag6 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory3)
                .name("자식분야6")
                .build();
        WorkFieldChildTag workFieldChildTag7 = WorkFieldChildTag.builder()
                .workFieldSubCategory(workFieldSubCategory4)
                .name("자식분야7")
                .build();

        workFieldChildTagRepository.save(workFieldChildTag1);
        workFieldChildTagRepository.save(workFieldChildTag2);
        workFieldChildTagRepository.save(workFieldChildTag3);
        workFieldChildTagRepository.save(workFieldChildTag4);
        workFieldChildTagRepository.save(workFieldChildTag5);
        workFieldChildTagRepository.save(workFieldChildTag6);
        workFieldChildTagRepository.save(workFieldChildTag7);


        Member member = getMember();

        //portfolio 1 저장
        Portfolio portfolio1 = Portfolio.builder().member(member).build();
        portfolioRepository.save(portfolio1);

        portfolio1.changeWorkFieldTagAndDescription(workFieldTag1,"나는 코딩을 잘합니다");
        portfolio1.getPortfolioImageList().add(
                PortfolioImage.builder().portfolio(portfolio1).originalName("원래이름1").accessUrl("엑세스1").build());
        portfolio1.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio1).originalName("원래이름2").accessUrl("엑세스2").build());
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag1 =
                PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag1).portfolio(portfolio1).build();
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag2 =
                PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag2).portfolio(portfolio1).build();
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag3 =
                PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag3).portfolio(portfolio1).build();
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag4 =
                PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag4).portfolio(portfolio1).build();

        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag1);
        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag2);
        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag3);
        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag4);


        //portfolio 2 저장
        Portfolio portfolio2 = Portfolio.builder().member(member).build();
        portfolioRepository.save(portfolio2);

        portfolio2.changeWorkFieldTagAndDescription(workFieldTag2,"나는 코쿤을 잘합니다");
        portfolio2.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio2).originalName("원래이름3").accessUrl("엑세스3").build());
        portfolio2.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio2).originalName("원래이름4").accessUrl("엑세스4").build());

        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag5 =
                PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag5).portfolio(portfolio2).build();
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag6 =
                PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag6).portfolio(portfolio2).build();
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag7 =
                PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag7).portfolio(portfolio2).build();

        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag5);
        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag6);
        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag7);


        //when
        portfolioWorkFieldChildTagRepository.deleteByPortfolio(portfolio1);
        portfolioWorkFieldChildTagRepository.deleteByPortfolio(portfolio2);
        List<PortfolioWorkFieldChildTag> byPortfolioIdWithFetchWorkFieldChildTag1=
                portfolioWorkFieldChildTagRepository.findByPortfolioIdWithFetchWorkFieldChildTag(
                        portfolio1.getId());

        List<PortfolioWorkFieldChildTag> byPortfolioIdWithFetchWorkFieldChildTag2 =
                portfolioWorkFieldChildTagRepository.findByPortfolioIdWithFetchWorkFieldChildTag(
                        portfolio2.getId());

        //then
        assertThat(byPortfolioIdWithFetchWorkFieldChildTag1.size()).isEqualTo(0);
        assertThat(byPortfolioIdWithFetchWorkFieldChildTag2.size()).isEqualTo(0);

    }


}