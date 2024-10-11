package com.gaduationproject.cre8.app.member.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.DuplicateException;
import com.gaduationproject.cre8.app.member.dto.LoginIdCheckResponseDto;
import com.gaduationproject.cre8.app.member.dto.MemberSignUpRequestDto;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.chat.repository.ChattingRoomRepository;
import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
import com.gaduationproject.cre8.domain.community.entity.Reply;
import com.gaduationproject.cre8.domain.community.repository.CommunityPostRepository;
import com.gaduationproject.cre8.domain.community.repository.LikeCommunityPostRepository;
import com.gaduationproject.cre8.domain.community.repository.ReplyRepository;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.repository.BookMarkEmployeePostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.BookMarkEmployerPostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployeePostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployeePostWorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployerPostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployerPostWorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import com.gaduationproject.cre8.domain.portfolio.repository.PortfolioImageRepository;
import com.gaduationproject.cre8.domain.portfolio.repository.PortfolioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final BookMarkEmployerPostRepository bookMarkEmployerPostRepository;
    private final BookMarkEmployeePostRepository bookMarkEmployeePostRepository;
    private final LikeCommunityPostRepository likeCommunityPostRepository;
    private final EmployerPostRepository employerPostRepository;
    private final EmployeePostRepository employeePostRepository;
    private final CommunityPostRepository communityPostRepository;
    private final ReplyRepository replyRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final EmployerPostWorkFieldChildTagRepository employerPostWorkFieldChildTagRepository;
    private final EmployeePostWorkFieldChildTagRepository employeePostWorkFieldChildTagRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioImageRepository portfolioImageRepository;




    @Transactional
    public Long saveMember(final MemberSignUpRequestDto memberSignUpRequestDto){

        checkDuplicateLoginId(memberSignUpRequestDto.getLoginId());

        if(memberRepository.existsByNickName(memberSignUpRequestDto.getNickName())){
            throw new DuplicateException(ErrorCode.DUPLICATE_NICKNAME);
        }

        checkDuplicateEmail(memberSignUpRequestDto.getEmail());



        Member member = Member.builder()
                .email(memberSignUpRequestDto.getEmail())
                .loginId(memberSignUpRequestDto.getLoginId())
                .name(memberSignUpRequestDto.getName())
                .sex(memberSignUpRequestDto.getSex())
                .password(passwordEncoder.encode(memberSignUpRequestDto.getPassword()))
                .nickName(memberSignUpRequestDto.getNickName())
                .birthDay(memberSignUpRequestDto.getBirthDay())
                .build();


        return memberRepository.save(member).getId();


    }

    @Transactional
    public void deleteMember(final String loginId){

        Member member = memberRepository.findMemberByLoginId(loginId)
                .orElseThrow(()-> new NotFoundException(ErrorCode.CANT_FIND_MEMBER));

        deleteMyReply(member);

        deleteMyPost(member);
        bookMarkEmployeePostRepository.deleteByMember(member);
        bookMarkEmployerPostRepository.deleteByMember(member);


        deleteMyCommunity(member);
        likeCommunityPostRepository.deleteByLiker(member);

        deleteMyPortfolio(member);


        chattingRoomRepository.deleteByReceiver(member);
        chattingRoomRepository.deleteBySender(member);


        memberRepository.delete(member);


    }

    private void deleteMyPortfolio(Member member) {

        List<Portfolio> portfolioList = portfolioRepository.findByMember(member);

        portfolioList.forEach(portfolio -> {
            portfolioImageRepository.deleteByPortfolio(portfolio);
        });

        portfolioRepository.deleteByMember(member);

    }

    private void deleteMyCommunity(Member member) {

        List<CommunityPost> myCommunity = communityPostRepository.findByWriter(member);

        myCommunity.forEach(communityPost -> {
            replyRepository.deleteByCommunityPostId(communityPost.getId());
            likeCommunityPostRepository.deleteByCommunityPostId(communityPost.getId());
        });

        communityPostRepository.deleteByWriter(member);

    }

    private void deleteMyPost(final Member member) {


        List<EmployerPost> myEmployerPost = employerPostRepository.findByBasicPostContent_Member(member);
        myEmployerPost.forEach(employerPost -> {
            bookMarkEmployerPostRepository.deleteByEmployerPostId(employerPost.getId());
            employerPostWorkFieldChildTagRepository.deleteByEmployerPost(employerPost);
        });

        List<EmployeePost> myEmployeePost = employeePostRepository.findByBasicPostContent_Member(member);
        myEmployeePost.forEach(employeePost -> {
            bookMarkEmployeePostRepository.deleteByEmployeePostId(employeePost.getId());
            employeePostWorkFieldChildTagRepository.deleteByEmployeePost(employeePost);
        });

        employerPostRepository.deleteByBasicPostContent_Member(member);
        employeePostRepository.deleteByBasicPostContent_Member(member);

    }

    public void deleteMyReply(final Member member){

        List<Reply> findParentReply = replyRepository.findParentReplyAndMember(member);

        findParentReply.forEach(reply -> {
            replyRepository.deleteByParentReply(reply);
        });

        replyRepository.deleteByWriter(member);

    }

    private void checkDuplicateEmail(final String email){
        if(memberRepository.existsByEmail(email)){
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    private void checkDuplicateLoginId(final String loginId){
        if(memberRepository.existsByLoginId(loginId)){
            throw new DuplicateException(ErrorCode.DUPLICATE_LOGIN_ID);
        }
    }

    public LoginIdCheckResponseDto checkExistsLoginId(final String loginId) {

        checkDuplicateLoginId(loginId);

        return LoginIdCheckResponseDto.builder().loginIdChecked(true).build();
    }


}
