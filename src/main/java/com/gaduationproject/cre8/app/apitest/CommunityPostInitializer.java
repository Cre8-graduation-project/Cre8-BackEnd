//package com.gaduationproject.cre8.app.apitest;
//
//import com.gaduationproject.cre8.common.response.error.ErrorCode;
//import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
//import com.gaduationproject.cre8.domain.community.entity.CommunityBoard;
//import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
//import com.gaduationproject.cre8.domain.community.repository.CommunityBoardRepository;
//import com.gaduationproject.cre8.domain.community.repository.CommunityPostRepository;
//import com.gaduationproject.cre8.domain.member.entity.Member;
//import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
//import java.sql.PreparedStatement;
//import java.util.ArrayList;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//@RequiredArgsConstructor
//@Component
//public class CommunityPostInitializer implements ApplicationRunner {
//
//    private final CommunityPostRepository communityPostRepository;
//    private final CommunityBoardRepository communityBoardRepository;
//    private final MemberRepository memberRepository;
//    private final JdbcTemplate jdbcTemplate;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//
//        List<CommunityPost> communityPosts  = new ArrayList<>();
//
//        Member member = memberRepository.findMemberByLoginId("dionisos198").orElseThrow(()-> new NotFoundException(ErrorCode.CANT_FIND_MEMBER));
//        CommunityBoard communityBoard = communityBoardRepository.findById(1L).orElseThrow(()-> new NotFoundException(ErrorCode.CANT_FIND_COMMUNITY_BOARD));
//
//
//
////10만건의 Member 데이터를 저장한다.
//        for(int i=0;i<50000;i++) {
//
//            CommunityPost communityPost = CommunityPost.builder()
//                    .communityBoard(communityBoard)
//                    .title("테스트 커뮤니티 제목")
//                    .accessUrl("www.")
//                    .contents("테스트 커뮤니티 콘텐츠입니당")
//                    .writer(member)
//                    .build();
//
//            System.out.println("현재 i:" + i);
//            communityPosts.add(communityPost);
//
//            if (i % 5000 == 0) {
//                String sql =
//                        "INSERT INTO community_post (contents,title,community_board_id,writer_id) "
//                                + "VALUES (?,?,?,?)";
//// jdbc 의 batch 를 이용해서 직접 대용량의 데이터를 저장한다. (GenerationType.Identity이므로)
//                jdbcTemplate.batchUpdate(sql,
//                        communityPosts, communityPosts.size(),
//                        (PreparedStatement ps, CommunityPost communityPost1) -> {
//                            ps.setString(1, communityPost1.getContents());
//                            ps.setString(2, communityPost1.getTitle());
//                            ps.setLong(3, communityPost1.getCommunityBoard().getId());
//                            ps.setLong(4, communityPost1.getWriter().getId());
//                        });
//
//                communityPosts.clear();
//            }
//        }
//        String sql =
//                "INSERT INTO community_post (contents,title,community_board_id,writer_id) "
//                        + "VALUES (?,?,?,?)";
//// jdbc 의 batch 를 이용해서 직접 대용량의 데이터를 저장한다. (GenerationType.Identity이므로)
//        jdbcTemplate.batchUpdate(sql,
//                communityPosts, communityPosts.size(),
//                (PreparedStatement ps, CommunityPost communityPost1) -> {
//                    ps.setString(1, communityPost1.getContents());
//                    ps.setString(2, communityPost1.getTitle());
//                    ps.setLong(3, communityPost1.getCommunityBoard().getId());
//                    ps.setLong(4, communityPost1.getWriter().getId());
//                });
//
//        communityPosts.clear();
//
//
//    }
//
//
//}
