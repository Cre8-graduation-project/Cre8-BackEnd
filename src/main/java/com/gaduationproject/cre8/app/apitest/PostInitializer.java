package com.gaduationproject.cre8.app.apitest;


import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePostWorkFieldChildTag;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPostWorkFieldChildTag;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployerPostRepository;
import com.gaduationproject.cre8.domain.employmentpost.type.EnrollDurationType;
import com.gaduationproject.cre8.domain.employmentpost.type.PaymentMethod;
import com.gaduationproject.cre8.domain.member.editor.MemberEditor;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.domain.member.type.Sex;
import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioWorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldTagRepository;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final WorkFieldTagRepository workFieldTagRepository;
    private final WorkFieldChildTagRepository workFieldChildTagRepository;
    private final JdbcTemplate jdbcTemplate;
    private final EmployerPostRepository employerPostRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<Member> memberList = new ArrayList<>();
        List<EmployerPost> employerPostList = new ArrayList<>();
        List<EmployerPostWorkFieldChildTag> employerPostWorkFieldChildTagList = new ArrayList<>();
        List<EmployeePost> employeePostList = new ArrayList<>();
        List<EmployeePostWorkFieldChildTag> employeePostWorkFieldChildTagList = new ArrayList<>();

        for(int i=0;i<10100;i++){

            System.out.println(i+"시작");

            Member member = Member.builder()
                    .name(generateRandomString(3,true))
                    .email(generateRandomString(8,false)+i+"@naver.com")
                    .loginId(generateRandomString(8,false)+i)
                    .sex(Sex.M)
                    .birthDay(LocalDate.of(2023,1,1))
                    .password(generateRandomString(5,false))
                    .nickName(generateRandomString(8,false)+i)
                    .build();

            member.edit(MemberEditor.builder()
                    .accessUrl("www.accessurl1.com")
                    .personalLink("www.personalLink.com")
                    .youtubeLink("www.youtube.com")
                    .twitterLink("www.twitter.com")
                    .personalStatement("안녕하세요! 저는 콘텐츠 크리에이터와 시청자를 연결하는 다리 역할을 하는 유튜브 편집자입니다. 영상 편집을 통해 시청자의 몰입도를 높이고, 크리에이터의 개성과 메시지를 극대화하는 것이 제 목표입니다.\n"
                            + "\n"
                            + "저는 Adobe Premiere Pro, After Effects 등 다양한 편집 툴에 능숙하며, 3년 이상의 유튜브 영상 편집 경험을 보유하고 있습니다. 트렌디한 효과와 음악을 적절히 활용해 콘텐츠를 시각적으로 매력적이면서도 메시지가 잘 전달되도록 편집하는 데 자신이 있습니다. 특히, 게임, 브이로그, 교육 콘텐츠 등 다양한 장르의 영상 편집을 진행하면서 각각의 콘텐츠 특성에 맞춘 스타일을 구현해 왔습니다.\n"
                            + "\n"
                            + "또한, 저는 단순히 편집만 하는 것이 아니라 크리에이터와의 소통을 통해 콘텐츠의 방향성과 목적을 이해하고, 이를 기반으로 최적의 편집을 제공하려고 노력합니다. 크리에이터가 원하는 분위기와 톤을 파악하고, 시청자의 반응을 고려한 편집 스타일을 제안하는 것이 저의 강점입니다.\n"
                            + "\n"
                            + "영상은 단순히 눈으로 보는 것이 아닌, 크리에이터의 스토리를 전하는 도구라고 생각합니다. 제가 편집한 영상이 시청자에게 재미와 감동을 전달하고, 크리에이터의 채널 성장을 돕는 중요한 요소가 되길 바랍니다. 이러한 목표를 이루기 위해 언제나 배우고 성장하는 자세로 임하고 있습니다.\n"
                            + "\n"
                            + "감사합니다!")
                            .nickName(generateRandomString(6,false))
                    .build());

            member.setId((long) i+200);


            memberList.add(member);

            List<Long> randomTagList = generateRandomTagList();

            EmployerPost employerPost = EmployerPost.builder()
                    .enrollDurationType(EnrollDurationType.ALWAYS)
                    .workFieldTag(workFieldTagRepository.findById(randomTagList.get(0)).get())
                    .companyName(generateRandomString(3,false)+"company")
                    .hopeCareerYear(getRandomInt(5,0))
                    .contact("mail")
                    .paymentAmount(getRandomInt(1300,0))
                    .paymentMethod(PaymentMethod.MONTH)
                    .accessUrl("https://search.pstatic.net/sunny/?src=https%3A%2F%2Fwww.dogdrip.net%2Fdvs%2Fd%2F24%2F04%2F07%2Fdcee16f4429c08e1e61d1ca453881187.jpg&type=ff332_332")
                    .title(generateRandomString(10,true)+"제목")
                    .contents("안녕하세요. 저는 창의적이고 문제 해결 능력이 뛰어난 소프트웨어 개발자입니다. 끊임없는 학습과 기술적 도전 속에서 성장하는 것을 즐기며, 최신 기술을 활용해 실질적인 문제를 해결하는 데 열정을 갖고 있습니다. 다양한 프로젝트를 통해 웹 개발, 모바일 앱 개발, 백엔드 시스템 설계 등 폭넓은 경험을 쌓았으며, 사용자 경험과 효율성에 중점을 둔 개발을 지향합니다.\n"
                            + "\n"
                            + "저는 Java, Python, JavaScript를 포함한 여러 언어에 능숙하며, 특히 Spring Boot와 Django를 사용한 웹 애플리케이션 개발에 자신이 있습니다. 프론트엔드에서는 React와 Vue.js 같은 프레임워크를 활용해 사용자 친화적인 인터페이스를 구현한 경험이 있습니다. 이러한 기술 스택을 통해 풀스택 개발자로서 여러 역할을 수행할 수 있습니다.\n"
                            + "\n"
                            + "또한, 데이터베이스 설계와 최적화에도 강한 관심을 가지고 있으며, MySQL과 PostgreSQL을 사용한 관계형 데이터베이스뿐만 아니라, MongoDB와 같은 NoSQL 데이터베이스에도 익숙합니다. 대용량 데이터를 처리하는 시스템에서 효율적인 쿼리와 데이터 구조 설계를 통해 성능을 개선하는 경험을 해왔습니다.\n"
                            + "\n"
                            + "팀워크를 중시하며, 협업 툴을 사용한 애자일 방식의 개발 프로세스에 익숙합니다. Git을 활용한 소스 관리와 CI/CD 파이프라인을 통한 자동화 배포 환경 구축에도 경험이 있어, 안정적이고 일관된 배포 프로세스를 유지하는 데 기여할 수 있습니다. 더불어, 프로젝트 관리 툴인 JIRA, Trello 등을 사용하여 팀과의 효율적인 커뮤니케이션과 일정 관리에 참여해왔습니다.\n"
                            + "\n"
                            + "무엇보다 저는 문제 해결 능력을 키우기 위해 항상 노력합니다. 복잡한 문제를 작은 단위로 나누어 해결하고, 사용자 관점에서 최선의 해결책을 제시하는 것을 목표로 합니다. 이러한 태도를 바탕으로 저는 항상 더 나은 코드를 작성하고, 더 나은 시스템을 설계하려고 노력합니다.\n"
                            + "\n"
                            + "새로운 기술에 대한 호기심이 많고, 배움을 멈추지 않는 저의 성향은 변화하는 기술 환경에서 제 자신을 발전시키는 원동력이 됩니다. 저는 성장할 수 있는 환경에서 도전 과제를 통해 스스로의 한계를 확장해 나가길 원합니다. 회사와 함께 발전하며, 제가 가진 기술과 경험을 활용해 가치 있는 기여를 하고 싶습니다.\n"
                            + "\n"
                            + "제 경험과 역량이 귀사의 프로젝트와 목표 달성에 기여할 수 있기를 바랍니다. 감사합니다.")
                    .deadLine(null)
                    .numberOfEmployee(getRandomInt(5,0))
                    .member(member)
                    .build();

            employerPost.setId((long)i+200);

            employerPostList.add(employerPost);

            EmployeePost employeePost = EmployeePost.builder()
                    .workFieldTag(workFieldTagRepository.findById(randomTagList.get(0)).get())
                    .careerYear(getRandomInt(5,0))
                    .contact("mail")
                    .paymentAmount(getRandomInt(1300,0))
                    .paymentMethod(PaymentMethod.MONTH)
                    .accessUrl("https://search.pstatic.net/sunny/?src=https%3A%2F%2Fwww.dogdrip.net%2Fdvs%2Fd%2F24%2F04%2F07%2Fdcee16f4429c08e1e61d1ca453881187.jpg&type=ff332_332")
                    .title(generateRandomString(10,true)+"제목")
                    .contents("안녕하세요. 저는 창의적이고 문제 해결 능력이 뛰어난 소프트웨어 개발자입니다. 끊임없는 학습과 기술적 도전 속에서 성장하는 것을 즐기며, 최신 기술을 활용해 실질적인 문제를 해결하는 데 열정을 갖고 있습니다. 다양한 프로젝트를 통해 웹 개발, 모바일 앱 개발, 백엔드 시스템 설계 등 폭넓은 경험을 쌓았으며, 사용자 경험과 효율성에 중점을 둔 개발을 지향합니다.\n"
                            + "\n"
                            + "저는 Java, Python, JavaScript를 포함한 여러 언어에 능숙하며, 특히 Spring Boot와 Django를 사용한 웹 애플리케이션 개발에 자신이 있습니다. 프론트엔드에서는 React와 Vue.js 같은 프레임워크를 활용해 사용자 친화적인 인터페이스를 구현한 경험이 있습니다. 이러한 기술 스택을 통해 풀스택 개발자로서 여러 역할을 수행할 수 있습니다.\n"
                            + "\n"
                            + "또한, 데이터베이스 설계와 최적화에도 강한 관심을 가지고 있으며, MySQL과 PostgreSQL을 사용한 관계형 데이터베이스뿐만 아니라, MongoDB와 같은 NoSQL 데이터베이스에도 익숙합니다. 대용량 데이터를 처리하는 시스템에서 효율적인 쿼리와 데이터 구조 설계를 통해 성능을 개선하는 경험을 해왔습니다.\n"
                            + "\n"
                            + "팀워크를 중시하며, 협업 툴을 사용한 애자일 방식의 개발 프로세스에 익숙합니다. Git을 활용한 소스 관리와 CI/CD 파이프라인을 통한 자동화 배포 환경 구축에도 경험이 있어, 안정적이고 일관된 배포 프로세스를 유지하는 데 기여할 수 있습니다. 더불어, 프로젝트 관리 툴인 JIRA, Trello 등을 사용하여 팀과의 효율적인 커뮤니케이션과 일정 관리에 참여해왔습니다.\n"
                            + "\n"
                            + "무엇보다 저는 문제 해결 능력을 키우기 위해 항상 노력합니다. 복잡한 문제를 작은 단위로 나누어 해결하고, 사용자 관점에서 최선의 해결책을 제시하는 것을 목표로 합니다. 이러한 태도를 바탕으로 저는 항상 더 나은 코드를 작성하고, 더 나은 시스템을 설계하려고 노력합니다.\n"
                            + "\n"
                            + "새로운 기술에 대한 호기심이 많고, 배움을 멈추지 않는 저의 성향은 변화하는 기술 환경에서 제 자신을 발전시키는 원동력이 됩니다. 저는 성장할 수 있는 환경에서 도전 과제를 통해 스스로의 한계를 확장해 나가길 원합니다. 회사와 함께 발전하며, 제가 가진 기술과 경험을 활용해 가치 있는 기여를 하고 싶습니다.\n"
                            + "\n"
                            + "제 경험과 역량이 귀사의 프로젝트와 목표 달성에 기여할 수 있기를 바랍니다. 감사합니다.")
                    .member(member)
                    .build();

            employeePost.setId((long) i+200);

            employeePostList.add(employeePost);

            for(int j=1;j<randomTagList.size();j++){
                EmployerPostWorkFieldChildTag employerPostWorkFieldChildTag = EmployerPostWorkFieldChildTag.builder().employerPost(employerPost)
                        .workFieldChildTag(workFieldChildTagRepository.findById(randomTagList.get(j)).get())
                        .build();

                employerPostWorkFieldChildTagList.add(employerPostWorkFieldChildTag);

                EmployeePostWorkFieldChildTag employeePostWorkFieldChildTag = EmployeePostWorkFieldChildTag.builder().employeePost(employeePost)
                        .workFieldChildTag(workFieldChildTagRepository.findById(randomTagList.get(j)).get())
                        .build();

                employeePostWorkFieldChildTagList.add(employeePostWorkFieldChildTag);
            }

            if(i%100==0){
                String sqlMember = "INSERT INTO member (access_url,authority,birth_day,login_id,name,nick_name,password,personal_link,"
                        + "personal_statement,sex,twitter_link,youtube_link,email,member_id) " +"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
// jdbc 의 batch 를 이용해서 직접 대용량의 데이터를 저장한다. (GenerationType.Identity이므로)
                jdbcTemplate.batchUpdate(sqlMember,
                        memberList,memberList.size(),
                        (PreparedStatement ps, Member checkMember )->{
                            ps.setString(1,checkMember.getAccessUrl());
                            ps.setString(2,checkMember.getAuthority().toString());
                            ps.setDate(3, Date.valueOf(checkMember.getBirthDay()));
                            ps.setString(4,checkMember.getLoginId());
                            ps.setString(5,checkMember.getName());
                            ps.setString(6,checkMember.getNickName());
                            ps.setString(7,checkMember.getPassword());
                            ps.setString(8,checkMember.getPersonalLink());
                            ps.setString(9,checkMember.getPersonalStatement());
                            ps.setString(10,checkMember.getSex().name());
                            ps.setString(11,checkMember.getTwitterLink());
                            ps.setString(12,checkMember.getYoutubeLink());
                            ps.setString(13, checkMember.getEmail());
                            ps.setLong(14,checkMember.getId());
                        });

                memberList.clear();

                String sqlEmployerPost = "INSERT INTO employer_post (created_at, updated_at, access_url, contact, contents, payment_amount, payment_method, title, "
                            + "company_name, dead_line, enroll_duration_type, hope_career_year, number_of_employee, member_id, work_field_tag_id,employer_post_id) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

// jdbc의 batch를 이용해 대용량 데이터를 저장
                    jdbcTemplate.batchUpdate(sqlEmployerPost,
                            employerPostList, employerPostList.size(),
                            (PreparedStatement ps, EmployerPost checkEmployerPost) -> {
                                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.of(2023,1,1,1,1)));  // LocalDateTime을 Timestamp로 변환
                                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.of(2023,1,1,1,1)));
                                ps.setString(3, checkEmployerPost.getBasicPostContent().getAccessUrl());
                                ps.setString(4, checkEmployerPost.getBasicPostContent().getContact());
                                ps.setString(5, checkEmployerPost.getBasicPostContent().getContents());
                                ps.setInt(6, checkEmployerPost.getBasicPostContent().getPayment().getPaymentAmount());
                                ps.setString(7, checkEmployerPost.getBasicPostContent().getPayment().getPaymentMethod().name());
                                ps.setString(8, checkEmployerPost.getBasicPostContent().getTitle());
                                ps.setString(9, checkEmployerPost.getCompanyName());
                                ps.setDate(10, null);  // LocalDate를 java.sql.Date로 변환
                                ps.setString(11, checkEmployerPost.getEnrollDurationType().name());
                                ps.setInt(12, checkEmployerPost.getHopeCareerYear());
                                ps.setInt(13, checkEmployerPost.getNumberOfEmployee());
                                ps.setLong(14, checkEmployerPost.getBasicPostContent().getMember().getId());  // 외래키로 연결된 member_id
                                ps.setLong(15, checkEmployerPost.getBasicPostContent().getWorkFieldTag().getId());  // 외래키로 연결된 work_field_tag_id
                                ps.setLong(16,checkEmployerPost.getId());
                            });

                    employerPostList.clear();


                String sqlEmployeePost = "INSERT INTO employee_post (created_at, updated_at, access_url, contact, contents, payment_amount, payment_method, title, "
                        + "career_year, member_id, work_field_tag_id,employee_post_id) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

// jdbc의 batch를 이용해 대용량 데이터를 저장
                jdbcTemplate.batchUpdate(sqlEmployeePost,
                        employeePostList, employeePostList.size(),
                        (PreparedStatement ps, EmployeePost checkEmployeePost) -> {
                            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.of(2023,1,1,1,1)));  // LocalDateTime을 Timestamp로 변환
                            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.of(2023,1,1,1,1)));
                            ps.setString(3, checkEmployeePost.getBasicPostContent().getAccessUrl());
                            ps.setString(4, checkEmployeePost.getBasicPostContent().getContact());
                            ps.setString(5, checkEmployeePost.getBasicPostContent().getContents());
                            ps.setInt(6, checkEmployeePost.getBasicPostContent().getPayment().getPaymentAmount());
                            ps.setString(7, checkEmployeePost.getBasicPostContent().getPayment().getPaymentMethod().name());
                            ps.setString(8, checkEmployeePost.getBasicPostContent().getTitle());
                            ps.setInt(9, checkEmployeePost.getCareerYear());
                            ps.setLong(10, checkEmployeePost.getBasicPostContent().getMember().getId());  // 외래키로 연결된 member_id
                            ps.setLong(11, checkEmployeePost.getBasicPostContent().getWorkFieldTag().getId());  // 외래키로 연결된 work_field_tag_id
                            ps.setLong(12,checkEmployeePost.getId());
                        });

                employeePostList.clear();

                String sqlEmployerPostWorkFieldChildTag =  "INSERT INTO employer_post_work_field_child_tag (employer_post_id, work_field_child_tag_id)"
                                + "VALUES (?, ?)";

                        jdbcTemplate.batchUpdate(sqlEmployerPostWorkFieldChildTag,
                                employerPostWorkFieldChildTagList, employerPostWorkFieldChildTagList.size(),
                                (PreparedStatement ps, EmployerPostWorkFieldChildTag checkEmployerPostWorkFieldChildTag) -> {
                                    ps.setLong(1,checkEmployerPostWorkFieldChildTag.getEmployerPost().getId());
                                    ps.setLong(2,checkEmployerPostWorkFieldChildTag.getWorkFieldChildTag().getId());
                                });

                employerPostWorkFieldChildTagList.clear();


                String sqlEmployeePostWorkFieldChildTag =  "INSERT INTO employee_post_work_field_child_tag (employee_post_id, work_field_child_tag_id)"
                        + "VALUES (?, ?)";

                jdbcTemplate.batchUpdate(sqlEmployeePostWorkFieldChildTag,
                        employeePostWorkFieldChildTagList, employeePostWorkFieldChildTagList.size(),
                        (PreparedStatement ps, EmployeePostWorkFieldChildTag checkEmployeePostWorkFieldChildTag) -> {
                            ps.setLong(1,checkEmployeePostWorkFieldChildTag.getEmployeePost().getId());
                            ps.setLong(2,checkEmployeePostWorkFieldChildTag.getWorkFieldChildTag().getId());
                        });

                employeePostWorkFieldChildTagList.clear();




            }




        }

        memberList.clear();
        employeePostList.clear();
        employerPostList.clear();
        employerPostWorkFieldChildTagList.clear();
        employeePostWorkFieldChildTagList.clear();

    }

    //이름을 만들때 랜덤하게 만들기 위한 메서드

    public static String generateRandomString(int length,boolean korean) {
        // 생성할 문자열에 포함될 문자들

        String characters = null;
        if(korean){
            characters="이김박우천하미푸풍라미투";
        }
        else{
            characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        }


        // SecureRandom 객체 생성
        SecureRandom random = new SecureRandom();

        // 랜덤 문자열 생성
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }

    public List<Long> generateRandomTagList(){

        List<Long> tagList = new ArrayList<>();
        Long workFieldTagId = getRandomValue(5,18);
        tagList.add(workFieldTagId);

        HashSet<Long> set =new HashSet<>();
        Long randomChildTagCounts = null;
        if(workFieldTagId==18){
             randomChildTagCounts = getRandomValue(6,1);

            for(int i=0;i<randomChildTagCounts;i++){
                set.add(getRandomValue(13,1));
            }
        }
        else if(workFieldTagId == 19){
            randomChildTagCounts = getRandomValue(3,1);

            for(int i=0;i<randomChildTagCounts;i++){
                set.add(getRandomValue(6,14));
            }
        }

        else if(workFieldTagId == 20){
            randomChildTagCounts = getRandomValue(2,1);

            for(int i=0;i<randomChildTagCounts;i++){
                set.add(getRandomValue(4,20));
            }
        }

        else if(workFieldTagId == 21){
            randomChildTagCounts = getRandomValue(2,1);

            for(int i=0;i<randomChildTagCounts;i++){
                set.add(getRandomValue(4,24));
            }
        }
        else if(workFieldTagId == 22){
            randomChildTagCounts = getRandomValue(3,1);

            for(int i=0;i<randomChildTagCounts;i++){
                set.add(getRandomValue(5,28));
            }
        }

        else if(workFieldTagId == 23){
            randomChildTagCounts = getRandomValue(2,1);

            for(int i=0;i<randomChildTagCounts;i++){
                set.add(getRandomValue(4,33));
            }
        }

        for(Long childTagId: set){
            tagList.add(childTagId);
        }

        return tagList;
    }

    public  Long getRandomValue(int size,int start) {
        Random random = new Random();

        return random.nextLong(size) + start;
    }

    public  Integer getRandomInt(int size,int start) {
        Random random = new Random();

        return random.nextInt(size) + start;
    }
}
