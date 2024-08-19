package com.gaduationproject.cre8.app.employmentpost.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.employmentpost.entity.BookMarkEmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployerPostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.BookMarkEmployerPostRepository;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookMarkEmployerPostService {

    private final EmployerPostRepository employerPostRepository;
    private final BookMarkEmployerPostRepository bookMarkEmployerPostRepository;
    private final MemberRepository memberRepository;



    //Post 에 대해 구독 를 누를 때를 위한 메서드
    @Transactional
    public void bookMarkPost(final String loginId,final Long employerPostId){

        final Member currentMember = getLoginMember(loginId);
        final EmployerPost employerPost = findEmployerPostById(employerPostId);


        //이미 눌러져 있을 때 deleteStareEmployerPost 수행, 없다면 saveStareEmployerPost 수행
        bookMarkEmployerPostRepository.findByMemberIdAndEmployerPostId(currentMember.getId(), employerPost.getId())
                .ifPresentOrElse(bookMarkEmployerPost -> {
                    cancelBookMarkEmployerPost(bookMarkEmployerPost);
                },()->{
                    saveBookMarkEmployerPost(currentMember,employerPost);
                });


    }


    // 구독 누를 시 사용 메서드
    private void saveBookMarkEmployerPost(final Member member,final EmployerPost employerPost){

        BookMarkEmployerPost bookMarkEmployerPost = BookMarkEmployerPost.builder()
                .employerPost(employerPost)
                .member(member)
                .build();

        bookMarkEmployerPostRepository.save(bookMarkEmployerPost);
    }

    //구독 한번 더 눌러 취소 시킬 때 사용 메서드
    private void cancelBookMarkEmployerPost(final BookMarkEmployerPost bookMarkEmployerPost){
        bookMarkEmployerPostRepository.delete(bookMarkEmployerPost);
    }

    private EmployerPost findEmployerPostById(final Long employerPostId){
        return employerPostRepository.findById(employerPostId).orElseThrow(()->new NotFoundException(
                ErrorCode.CANT_FIND_EMPLOYER_POST));
    }

    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.LOGIN_ID_NOT_MATCH));
    }

}
