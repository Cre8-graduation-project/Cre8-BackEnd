package com.gaduationproject.cre8.domain.employmentpost.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gaduationproject.cre8.domain.config.QueryDSLConfig;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePostWorkFieldChildTag;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployeePostSearch;
import com.gaduationproject.cre8.domain.employmentpost.type.PaymentMethod;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioImage;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioWorkFieldChildTag;
import com.gaduationproject.cre8.domain.portfolio.repository.PortfolioImageRepository;
import com.gaduationproject.cre8.domain.portfolio.repository.PortfolioRepository;
import com.gaduationproject.cre8.domain.portfolio.repository.PortfolioWorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldSubCategory;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldSubCategoryRepository;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldTagRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
class EmployeePostRepositoryTest {

    @Autowired
    EmployeePostRepository employeePostRepository;

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
    EmployeePostWorkFieldChildTagRepository employeePostWorkFieldChildTagRepository;

    public DefaultSettingTestDto makeDefaultSetting(){

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
        portfolio1.getPortfolioImageList().add(PortfolioImage.builder().portfolio(portfolio1).accessUrl("엑세스1").originalName("원래1").build());
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
        EmployeePost employeePost1 = EmployeePost.
                builder().workFieldTag(workFieldTag2)
                .member(member)
                .title("사람구해요1-제목")
                .paymentMethod(PaymentMethod.toPaymentMethodEnum("작업물 건 당 지급"))
                .paymentAmount(1600)
                .careerYear(10)
                .contents("사람구해요1-내용")
                .contact("dionisos198@naver.com")
                .build();

        EmployeePostWorkFieldChildTag employeePostWorkFieldChildTag1 = EmployeePostWorkFieldChildTag
                .builder()
                .employeePost(employeePost1)
                .workFieldChildTag(workFieldChildTag7)
                .build();

        EmployeePostWorkFieldChildTag employeePostWorkFieldChildTag2 = EmployeePostWorkFieldChildTag
                .builder()
                .employeePost(employeePost1)
                .workFieldChildTag(workFieldChildTag6)
                .build();

        employeePostRepository.save(employeePost1);
        employeePostWorkFieldChildTagRepository.save(employeePostWorkFieldChildTag1);
        employeePostWorkFieldChildTagRepository.save(employeePostWorkFieldChildTag2);


        //구인글 - 2
        EmployeePost employeePost2 = EmployeePost.
                builder().workFieldTag(workFieldTag1)
                .member(member)
                .title("사람구해요2-제목")
                .paymentMethod(PaymentMethod.toPaymentMethodEnum("월급"))
                .paymentAmount(1600)
                .careerYear(5)
                .contents("사람구해요2-내용")
                .contact("dionisos1988@naver.com")
                .build();

        EmployeePostWorkFieldChildTag employeePostWorkFieldChildTag3 = EmployeePostWorkFieldChildTag
                .builder()
                .employeePost(employeePost2)
                .workFieldChildTag(workFieldChildTag1)
                .build();


        employeePostRepository.save(employeePost2);
        employeePostWorkFieldChildTagRepository.save(employeePostWorkFieldChildTag3);


        //구인글 - 3
        EmployeePost employeePost3 = EmployeePost.
                builder().workFieldTag(null)
                .member(member)
                .title("사람구해요3-제목")
                .paymentMethod(PaymentMethod.toPaymentMethodEnum("작업물 분 당 지급"))
                .paymentAmount(1600)
                .careerYear(0)
                .contents("사람구해요3-내용")
                .contact("dionisos19888@naver.com")
                .build();


        employeePostRepository.save(employeePost3);


        DefaultSettingTestDto defaultSettingTestDto = new DefaultSettingTestDto();

        defaultSettingTestDto.employPostIdList.add(employeePost1.getId());
        defaultSettingTestDto.employPostIdList.add(employeePost2.getId());
        defaultSettingTestDto.employPostIdList.add(employeePost3.getId());

        defaultSettingTestDto.employWorkFieldTagIdList.add(workFieldTag1.getId());
        defaultSettingTestDto.employWorkFieldTagIdList.add(workFieldTag2.getId());

        defaultSettingTestDto.workFieldChildList.add(workFieldChildTag1.getId());
        defaultSettingTestDto.workFieldChildList.add(workFieldChildTag2.getId());
        defaultSettingTestDto.workFieldChildList.add(workFieldChildTag3.getId());
        defaultSettingTestDto.workFieldChildList.add(workFieldChildTag4.getId());
        defaultSettingTestDto.workFieldChildList.add(workFieldChildTag5.getId());
        defaultSettingTestDto.workFieldChildList.add(workFieldChildTag6.getId());
        defaultSettingTestDto.workFieldChildList.add(workFieldChildTag7.getId());


        return defaultSettingTestDto;

    }

    public class DefaultSettingTestDto{

        List<Long> employPostIdList = new ArrayList<>();

        List<Long> employWorkFieldTagIdList= new ArrayList<>();

        List<Long> workFieldChildList = new ArrayList<>();

    }


    @Test
    @DisplayName("EmployeePost - 단일조회")
    public void 구직게시글_단일_조회(){

        //given
        DefaultSettingTestDto defaultSettingTestDto = makeDefaultSetting();


        //when
        Optional<EmployeePost> byIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag = employeePostRepository.findByIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag
                (defaultSettingTestDto.employPostIdList.get(0));


        //then
        assertThat(byIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag).isNotNull();
        assertThat(byIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag.get()
                .getCareerYear()).isEqualTo(10);
        assertThat(byIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag.get()
                .getBasicPostContent().getContents()).isEqualTo("사람구해요1-내용");
        assertThat(byIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag.get()
                .getBasicPostContent().getTitle()).isEqualTo("사람구해요1-제목");
        assertThat(byIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag.get()
                .getBasicPostContent().getContact()).isEqualTo("dionisos198@naver.com");
        assertThat(byIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag.get()
                .getBasicPostContent().getPayment().getPaymentAmount()).isEqualTo(1600);
        assertThat(byIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag.get()
                .getBasicPostContent().getPayment().getPaymentMethod()).isEqualTo(PaymentMethod.PER_PIECE);
        assertThat(byIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag.get()
                .getBasicPostContent().getWorkFieldTag().getName()).isEqualTo("작업분야2");
        assertThat(byIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag.get()
                .getEmployeePostWorkFieldChildTagList().size()).isEqualTo(2);




    }

    @Test
    @DisplayName("EmployeePost - 동적쿼리 검색")
    public void 구직게시글_태그_특성_기반_검색(){

        //given
        DefaultSettingTestDto defaultSettingTestDto = makeDefaultSetting();


        //when
        EmployeePostSearch employeePostSearch1 = new EmployeePostSearch(null,null,0,7);
        Page<EmployeePost> employeePosts1 = employeePostRepository.showEmployeePostListWithPage(
                employeePostSearch1, PageRequest.of(0, 1));

        EmployeePostSearch employeePostSearch2 = new EmployeePostSearch(defaultSettingTestDto.employWorkFieldTagIdList.get(0), null,null,null);
        Page<EmployeePost> employeePosts2 = employeePostRepository.showEmployeePostListWithPage(
                employeePostSearch2, PageRequest.of(0, 1));

        EmployeePostSearch employeePostSearch3 = new EmployeePostSearch(defaultSettingTestDto.employWorkFieldTagIdList.get(0),
                List.of(defaultSettingTestDto.workFieldChildList.get(0)),null,null);
        Page<EmployeePost> employeePosts3 = employeePostRepository.showEmployeePostListWithPage(
                employeePostSearch3, PageRequest.of(0, 1));



        //then
        assertThat(employeePosts1.getTotalPages()).isEqualTo(2);
        assertThat(employeePosts1.getContent().get(0).getCareerYear()).isEqualTo(5);
        assertThat(employeePosts2.getTotalPages()).isEqualTo(1);
        assertThat(employeePosts2.getContent().get(0).getBasicPostContent().getTitle()).isEqualTo("사람구해요2-제목");
        assertThat(employeePosts3.getTotalPages()).isEqualTo(1);
        assertThat(employeePosts3.getContent().get(0).getBasicPostContent().getTitle()).isEqualTo("사람구해요2-제목");



    }




}