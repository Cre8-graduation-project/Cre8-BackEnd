package com.gaduationproject.cre8.app.employmentpost.service;

import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchWithSliceResponseDto;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeePostKeyWordSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.repository.BookMarkEmployeePostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployeePostWorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployeePostSearch;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchWithCountResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployeePostRepository;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmployeePostSearchService {

    private final EmployeePostRepository employeePostRepository;
    private final MemberRepository memberRepository;
    private final BookMarkEmployeePostRepository bookMarkEmployeePostRepository;
    private final EmployeePostWorkFieldChildTagRepository employeePostWorkFieldChildTagRepository;



    public EmployeePostSearchWithCountResponseDto searchEmployeePost(final EmployeePostSearch employerPostSearch,
            final Pageable pageable){

        Page<EmployeePost> employeePostSearchResponseDtoPage =
                employeePostRepository.showEmployeePostListWithPage(employerPostSearch,pageable);

        return EmployeePostSearchWithCountResponseDto.of(employeePostSearchResponseDtoPage.getTotalElements(),
                employeePostSearchResponseDtoPage.getContent().stream().map(employeePost -> {
                    List<String> tagNameList = getTagList(employeePost);

                    return EmployeePostSearchResponseDto.of(employeePost,tagNameList);

                }).collect(
                        Collectors.toList()),employeePostSearchResponseDtoPage.getTotalPages());
    }

    public EmployeePostSearchWithCountResponseDto searchEmployeePostWithDto(final EmployeePostSearch employeePostSearch,
            final Pageable pageable){

        Page<EmployeeSearchDBResponseDto> employeePostSearchResponseDtoPage =
                employeePostRepository.showEmployeePostDtoListWithPage(employeePostSearch,pageable);

        return EmployeePostSearchWithCountResponseDto.of(employeePostSearchResponseDtoPage.getTotalElements(),
                employeePostSearchResponseDtoPage.getContent().stream().map(
                        employeeSearchDBResponseDto -> {
                    List<String> tagNameList = getTagListWithFetchTag(employeeSearchDBResponseDto);

                    return EmployeePostSearchResponseDto.ofFaster(employeeSearchDBResponseDto,tagNameList);

                }).collect(
                        Collectors.toList()),employeePostSearchResponseDtoPage.getTotalPages());
    }



    public EmployeePostSearchWithCountResponseDto searchEmployeeByKeyword(final String keyword,final Pageable pageable){

//        Slice<EmployeePost> employeePostSlice =
//                employeePostRepository.findEmployeePostWithFetchMemberAndWorkFieldTagAndChildTagListWithSlice(keyword,pageable);
//
//        return EmployeePostSearchWithSliceResponseDto.of(employeePostSlice.getContent().stream().map(employeePost -> {
//            List<String> tagNameList = getTagList(employeePost);
//
//            return EmployeePostSearchResponseDto.of(employeePost,tagNameList);
//
//        }).collect(Collectors.toList()), employeePostSlice.hasNext());

        Page<Long> employeePostIdList = employeePostRepository.findEmployeePostIdWithPage(keyword,pageable);

        List<EmployeePostKeyWordSearchDBResponseDto> employeePostList =
                employeePostRepository.findEmployeePostKeyWordSearchDB(employeePostIdList.getContent(),pageable.getSort());

        return EmployeePostSearchWithCountResponseDto.of(employeePostIdList.getTotalElements(),
                employeePostList.stream().map(employeePostKeyWordSearchDBResponseDto -> {

                    return EmployeePostSearchResponseDto.ofSearch(employeePostKeyWordSearchDBResponseDto,
                            getTagListByEmployeePostWorkField(
                                    employeePostKeyWordSearchDBResponseDto));
                }
                ).collect(Collectors.toList()),employeePostIdList.getTotalPages());



    }

    public EmployeePostSearchWithSliceResponseDto searchMyEmployeePost(final String loginId,final Pageable pageable){

        Member member = getLoginMember(loginId);

        Slice<EmployeePost> employeePostSlice =
                employeePostRepository.findEmployeePostByMemberId(member.getId(),pageable);

        return EmployeePostSearchWithSliceResponseDto.of(employeePostSlice.getContent().stream().map(employeePost -> {

            List<String> tagNameList = getTagList(employeePost);

            return EmployeePostSearchResponseDto.of(employeePost,tagNameList);
        }).collect(Collectors.toList()), employeePostSlice.hasNext());
    }

    public EmployeePostSearchWithSliceResponseDto searchMyBookMarkEmployeePost(final String loginId,final Pageable pageable){

        Member member = getLoginMember(loginId);

        Slice<EmployeePost> bookMarkEmployeePostSlice =
                bookMarkEmployeePostRepository.showMyBookMarkEmployeePost(member.getId(),pageable).map(bookMarkEmployeePost -> bookMarkEmployeePost.getEmployeePost());

        return EmployeePostSearchWithSliceResponseDto.of(bookMarkEmployeePostSlice.getContent().stream().map(employeePost -> {

            List<String> tagNameList = getTagList(employeePost);

            return EmployeePostSearchResponseDto.of(employeePost,tagNameList);
        }).collect(Collectors.toList()), bookMarkEmployeePostSlice.hasNext());
    }

    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.CANT_FIND_MEMBER));
    }

    private  List<String> getTagList(final EmployeePost employeePost) {
        List<String> tagNameList = new ArrayList<>();

        if(employeePost.getBasicPostContent().getWorkFieldTag()!=null){
            tagNameList.add(employeePost.getBasicPostContent().getWorkFieldTag().getName());
        }

        employeePost.getEmployeePostWorkFieldChildTagList().forEach(employeePostWorkFieldChildTag -> {
            tagNameList.add(employeePostWorkFieldChildTag.getWorkFieldChildTag().getName());
        });

        return tagNameList;
    }

    private List<String> getTagListWithFetchTag(final EmployeeSearchDBResponseDto employeeSearchDBResponseDto){

        List<String> tagNameList = new ArrayList<>();

        if(employeeSearchDBResponseDto.getWorkFieldTag()!=null){
            tagNameList.add(employeeSearchDBResponseDto.getWorkFieldTag().getName());
        }

        employeeSearchDBResponseDto.getEmployeePostWorkFieldChildTagSearchDBResponseDtoList().forEach(employeePostWorkFieldChildTagSearchResponseDto -> {
            tagNameList.add(employeePostWorkFieldChildTagSearchResponseDto.getChildTagName());
        });


        return tagNameList;

    }

    private List<String> getTagListByEmployeePostWorkField(final
    EmployeePostKeyWordSearchDBResponseDto employeePostKeyWordSearchDBResponseDto){

        List<String> tagNameList = new ArrayList<>();

        if(employeePostKeyWordSearchDBResponseDto.getWorkFieldTag()!=null){
            tagNameList.add(employeePostKeyWordSearchDBResponseDto.getWorkFieldTag().getName());
        }

        employeePostWorkFieldChildTagRepository.findByEmployeePost_Id(employeePostKeyWordSearchDBResponseDto.getEmployeePostId())
                .forEach(employeePostWorkFieldChildTag -> tagNameList.add(employeePostWorkFieldChildTag.getWorkFieldChildTag().getName()));

        return tagNameList;

    }





}
