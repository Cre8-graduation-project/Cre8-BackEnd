package com.gaduationproject.cre8.app.employmentpost.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.employmentpost.entity.BookMarkEmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployeePostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.BookMarkEmployeePostRepository;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookMarkEmployeePostService {

    private final EmployeePostRepository employeePostRepository;
    private final BookMarkEmployeePostRepository bookMarkEmployeePostRepository;
    private final MemberRepository memberRepository;



    //Post 에 대해 구독 를 누를 때를 위한 메서드
    @Transactional
    public void bookMarkPost(final String loginId,final Long employeePostId){

        final Member currentMember = getLoginMember(loginId);
        final EmployeePost employeePost = findEmployeePostById(employeePostId);


        //이미 눌러져 있을 때 deleteBookMarkEmployeePost 수행, 없다면 saveBookMarkEmployeePost 수행
        bookMarkEmployeePostRepository.findByMemberIdAndEmployeePostId(currentMember.getId(), employeePost.getId())
                .ifPresentOrElse(bookMarkEmployeePost -> {
                    cancelBookMarkEmployeePost(bookMarkEmployeePost);
                },()->{
                    saveBookMarkEmployeePost(currentMember,employeePost);
                });


    }


    // 구독 누를 시 사용 메서드
    private void saveBookMarkEmployeePost(final Member member,final EmployeePost employeePost){

        BookMarkEmployeePost bookMarkEmployeePost = BookMarkEmployeePost.builder()
                        .employeePost(employeePost)
                                .member(member)
                                        .build();

        bookMarkEmployeePostRepository.save(bookMarkEmployeePost);
    }

    //구독 한번 더 눌러 취소 시킬 때 사용 메서드
    private void cancelBookMarkEmployeePost(final BookMarkEmployeePost bookMarkEmployeePost){
        bookMarkEmployeePostRepository.delete(bookMarkEmployeePost);
    }

    private EmployeePost findEmployeePostById(final Long employeePostId){
        return employeePostRepository.findById(employeePostId).orElseThrow(()->new NotFoundException(
                ErrorCode.CANT_FIND_EMPLOYEE_POST));
    }

    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.LOGIN_ID_NOT_MATCH));
    }

}
