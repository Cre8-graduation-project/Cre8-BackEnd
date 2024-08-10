package com.gaduationproject.cre8.domain.employmentpost.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gaduationproject.cre8.domain.config.QueryDSLConfig;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePostWorkFieldChildTag;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPostWorkFieldChildTag;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployeePostRepositoryTest.DefaultSettingTestDto;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployeePostSearch;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployerPostSearch;
import com.gaduationproject.cre8.domain.employmentpost.type.EnrollDurationType;
import com.gaduationproject.cre8.domain.employmentpost.type.PaymentMethod;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioImage;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioWorkFieldChildTag;
import com.gaduationproject.cre8.domain.portfolio.repository.PortfolioRepository;
import com.gaduationproject.cre8.domain.portfolio.repository.PortfolioWorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldSubCategory;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldSubCategoryRepository;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldTagRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY) //Any : In memory db ,None: 실제 사용하는 datasource 사용
@Import({QueryDSLConfig.class})
class EmployerPostRepositoryTest {

    @Autowired
    EmployerPostRepository employerPostRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    WorkFieldTagRepository workFieldTagRepository;

    @Autowired
    WorkFieldSubCategoryRepository workFieldSubCategoryRepository;

    @Autowired
    WorkFieldChildTagRepository workFieldChildTagRepository;

    @Autowired
    PortfolioWorkFieldChildTagRepository portfolioWorkFieldChildTagRepository;

    @Autowired
    EmployerPostWorkFieldChildTagRepository employerPostWorkFieldChildTagRepository;

    public DefaultSettingTestDto1 makeDefaultSetting(){

        // 사람
        Member member = Member.builder()
                .nickName("dionisos198")
                .email("dionisos198@naver.com")
                .name("이진우")
                .loginId("dionisos198")
                .password("ppp")
                .sex(Sex.M)
                .birthDay(LocalDate.of(2023,1,1))
                .build();

        memberRepository.save(member);

        //작업분야
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

        //포트폴리오
        Portfolio portfolio1 = Portfolio.builder().member(member).build();
        portfolio1.changeWorkFieldTagAndDescription(workFieldTag1,"첫번째 작업에 대한 설명");
        portfolio1.getPortfolioImageList().add(
                PortfolioImage.builder().portfolio(portfolio1).accessUrl("엑세스1").originalName("원래1").build());
        portfolio1.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio1).accessUrl("엑세스2").originalName("원래2").build());

        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag1 = PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag1)
                .portfolio(portfolio1).build();

        portfolioRepository.save(portfolio1);
        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag1);

        Portfolio portfolio2 = Portfolio.builder().member(member).build();
        portfolio2.changeWorkFieldTagAndDescription(workFieldTag1,"두번째 작업에 대한 설명");
        portfolio2.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio2).accessUrl("엑세스3").originalName("원래3").build());
        PortfolioWorkFieldChildTag portfolioWorkFieldChildTag2 = PortfolioWorkFieldChildTag.builder().workFieldChildTag(workFieldChildTag2)
                .portfolio(portfolio2).build();

        portfolioRepository.save(portfolio2);
        portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag2);

        Portfolio portfolio3 = Portfolio.builder().member(member).build();
        portfolio3.changeWorkFieldTagAndDescription(null,"세번째 작업에 대한 설명");

        portfolioRepository.save(portfolio1);


        // 구인글 - 1
        EmployerPost employerPost1 = EmployerPost.
                builder().workFieldTag(workFieldTag2)
                .member(member)
                .title("사람구해요1-제목")
                .contents("사람구해요1-내용")
                .enrollDurationType(EnrollDurationType.ALWAYS)
                .hopeCareerYear(3)
                .deadLine(null)
                .companyName("지누컴퍼니1")
                .contact("dionisos198@naver.com")
                .workFieldTag(workFieldTag1)
                .paymentMethod(PaymentMethod.MONTH)
                .paymentAmount(800)
                .build();

        EmployerPostWorkFieldChildTag employerPostWorkFieldChildTag1 = EmployerPostWorkFieldChildTag
                .builder()
                .employerPost(employerPost1)
                .workFieldChildTag(workFieldChildTag7)
                .build();

        EmployerPostWorkFieldChildTag employerPostWorkFieldChildTag2 = EmployerPostWorkFieldChildTag
                .builder()
                .employerPost(employerPost1)
                .workFieldChildTag(workFieldChildTag6)
                .build();

        employerPostRepository.save(employerPost1);
        employerPostWorkFieldChildTagRepository.save(employerPostWorkFieldChildTag1);
        employerPostWorkFieldChildTagRepository.save(employerPostWorkFieldChildTag2);


        //구인글 - 2
        EmployerPost employerPost2 = EmployerPost.
                builder().workFieldTag(workFieldTag1)
                .member(member)
                .title("사람구해요2-제목")
                .contents("사람구해요2-내용")
                .enrollDurationType(EnrollDurationType.DEAD_LINE)
                .hopeCareerYear(0)
                .deadLine(LocalDate.of(2023,1,1))
                .companyName("지누컴퍼니2")
                .contact("dionisos1988@naver.com")
                .workFieldTag(workFieldTag2)
                .paymentMethod(PaymentMethod.toPaymentMethodEnum("월급"))
                .paymentAmount(1600)
                .build();

        EmployerPostWorkFieldChildTag employerPostWorkFieldChildTag3 = EmployerPostWorkFieldChildTag
                .builder()
                .employerPost(employerPost2)
                .workFieldChildTag(workFieldChildTag7)
                .build();


        employerPostRepository.save(employerPost2);
        employerPostWorkFieldChildTagRepository.save(employerPostWorkFieldChildTag3);


        //구인글 - 3
        EmployerPost employerPost3 = EmployerPost.
                builder().workFieldTag(workFieldTag1)
                .member(member)
                .title("사람구해요3-제목")
                .contents("사람구해요3-내용")
                .enrollDurationType(EnrollDurationType.ALWAYS)
                .hopeCareerYear(20)
                .deadLine(null)
                .companyName("지누컴퍼니3")
                .contact("dionisos19888@naver.com")
                .workFieldTag(null)
                .paymentMethod(PaymentMethod.toPaymentMethodEnum("월급"))
                .paymentAmount(1600)
                .build();


        employerPostRepository.save(employerPost3);



        DefaultSettingTestDto1 defaultSettingTestDto1 = new DefaultSettingTestDto1();

        defaultSettingTestDto1.employerPostIdList.add(employerPost1.getId());
        defaultSettingTestDto1.employerPostIdList.add(employerPost2.getId());
        defaultSettingTestDto1.employerPostIdList.add(employerPost3.getId());

        defaultSettingTestDto1.employerWorkFieldTagIdList.add(workFieldTag1.getId());
        defaultSettingTestDto1.employerWorkFieldTagIdList.add(workFieldTag2.getId());

        defaultSettingTestDto1.workFieldChildList.add(workFieldChildTag1.getId());
        defaultSettingTestDto1.workFieldChildList.add(workFieldChildTag2.getId());
        defaultSettingTestDto1.workFieldChildList.add(workFieldChildTag3.getId());
        defaultSettingTestDto1.workFieldChildList.add(workFieldChildTag4.getId());
        defaultSettingTestDto1.workFieldChildList.add(workFieldChildTag5.getId());
        defaultSettingTestDto1.workFieldChildList.add(workFieldChildTag6.getId());
        defaultSettingTestDto1.workFieldChildList.add(workFieldChildTag7.getId());


        return defaultSettingTestDto1;

    }

    public class DefaultSettingTestDto1{

        List<Long> employerPostIdList = new ArrayList<>();

        List<Long> employerWorkFieldTagIdList= new ArrayList<>();

        List<Long> workFieldChildList = new ArrayList<>();

    }

    @Test
    @DisplayName("EmployeePost - 단일조회")
    public void 구인게시글_단일_조회(){

        //given
        DefaultSettingTestDto1 defaultSettingTestDto1 = makeDefaultSetting();


        //when
        Optional<EmployerPost> byIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag = employerPostRepository.findByIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag(
                defaultSettingTestDto1.employerPostIdList.get(0));

        //then
        assertThat(byIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag).isNotNull();
        assertThat(byIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag.get()
                .getHopeCareerYear()).isEqualTo(3);
        assertThat(byIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag.get()
                .getBasicPostContent().getContents()).isEqualTo("사람구해요1-내용");
        assertThat(byIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag.get()
                .getBasicPostContent().getTitle()).isEqualTo("사람구해요1-제목");
        assertThat(byIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag.get()
                .getBasicPostContent().getContact()).isEqualTo("dionisos198@naver.com");
        assertThat(byIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag.get()
                .getBasicPostContent().getPayment().getPaymentAmount()).isEqualTo(800);
        assertThat(byIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag.get()
                .getBasicPostContent().getPayment().getPaymentMethod()).isEqualTo(PaymentMethod.MONTH);
        assertThat(byIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag.get()
                .getBasicPostContent().getWorkFieldTag().getName()).isEqualTo("작업분야1");
        assertThat(byIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag.get()
                .getEmployerPostWorkFieldChildTagList().size()).isEqualTo(2);




    }

    @Test
    @DisplayName("EmployeePost - 동적쿼리 검색")
    public void 구인게시글_태그_특성_기반_검색(){

        //given
        DefaultSettingTestDto1 defaultSettingTestDto1 = makeDefaultSetting();


        //when
        EmployerPostSearch employerPostSearch1 = new EmployerPostSearch(defaultSettingTestDto1.employerPostIdList.get(0),null,0,50);
        Page<EmployerPost> employerPost1 = employerPostRepository.showEmployerPostListWithPage(
                employerPostSearch1, PageRequest.of(0, 1));

        EmployerPostSearch employerPostSearch2 = new EmployerPostSearch(defaultSettingTestDto1.employerPostIdList.get(0),null,-2,-1);
        Page<EmployerPost> employerPost2 = employerPostRepository.showEmployerPostListWithPage(
                employerPostSearch2, PageRequest.of(0, 1));

        EmployerPostSearch employerPostSearch3 = new EmployerPostSearch(defaultSettingTestDto1.employerPostIdList.get(0),
                List.of(defaultSettingTestDto1.workFieldChildList.get(3)),null,null);
        Page<EmployerPost> employerPost3 = employerPostRepository.showEmployerPostListWithPage(
                employerPostSearch3, PageRequest.of(0, 1));

        EmployerPostSearch employerPostSearch4 = new EmployerPostSearch(defaultSettingTestDto1.employerPostIdList.get(0),
                List.of(defaultSettingTestDto1.workFieldChildList.get(6)),null,null);
        Page<EmployerPost> employerPost4 = employerPostRepository.showEmployerPostListWithPage(
                employerPostSearch4, PageRequest.of(0, 1));



        //then
        assertThat(employerPost1.getTotalPages()).isEqualTo(1);
        assertThat(employerPost1.getContent().get(0).getHopeCareerYear()).isEqualTo(3);
        assertThat(employerPost2.getTotalPages()).isEqualTo(0);
        assertThat(employerPost3.getTotalPages()).isEqualTo(0);
        assertThat(employerPost4.getTotalPages()).isEqualTo(1);




    }

}