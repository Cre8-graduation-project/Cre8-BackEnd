package com.gaduationproject.cre8.app.employmentpost.service;

import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchWithCountResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostSearchWithSliceResponseDto;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeePostKeyWordSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployerPostKeyWordSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployerSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.repository.BookMarkEmployerPostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployerPostWorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployerPostSearch;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostSearchResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostSearchWithCountResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployerPostRepository;
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
public class EmployerPostSearchService {

    private final EmployerPostRepository employerPostRepository;
    private final MemberRepository memberRepository;
    private final BookMarkEmployerPostRepository bookMarkEmployerPostRepository;
    private final EmployerPostWorkFieldChildTagRepository employerPostWorkFieldChildTagRepository;




    public EmployerPostSearchWithCountResponseDto searchEmployerPost(final EmployerPostSearch employerPostSearch,
            final Pageable pageable){

        Page<EmployerPost> employerPostSearchResponseDtoPage =
                employerPostRepository.showEmployerPostListWithPage(employerPostSearch,pageable);

        return EmployerPostSearchWithCountResponseDto.of(employerPostSearchResponseDtoPage.getTotalElements(),
                employerPostSearchResponseDtoPage.getContent().stream().map(employerPost -> {
                    List<String> tagNameList = getTagList(employerPost);

                    return EmployerPostSearchResponseDto.of(employerPost,tagNameList);

                }).collect(
                        Collectors.toList()),employerPostSearchResponseDtoPage.getTotalPages());
    }

    public EmployerPostSearchWithCountResponseDto searchEmployerPostWithDto(final EmployerPostSearch employerPostSearch,
            final Pageable pageable){

        Page<EmployerSearchDBResponseDto> employerPostSearchResponseDtoPage =
                employerPostRepository.showEmployerPostDtoListWithPage(employerPostSearch,pageable);

        return EmployerPostSearchWithCountResponseDto.of(employerPostSearchResponseDtoPage.getTotalElements(),
                employerPostSearchResponseDtoPage.getContent().stream().map(
                        employerSearchDBResponseDto -> {
                            List<String> tagNameList = getTagListWithFetchTag(employerSearchDBResponseDto);

                            return EmployerPostSearchResponseDto.ofFaster(employerSearchDBResponseDto,tagNameList);

                        }).collect(
                        Collectors.toList()),employerPostSearchResponseDtoPage.getTotalPages());
    }



    public EmployerPostSearchWithCountResponseDto searchEmployerPostByKeyWord(final String keyword,
            final Pageable pageable) {

//        Slice<EmployerPost> employerPostSlice =
//                employerPostRepository.findEmployerPostWithFetchMemberAndWorkFieldTagAndChildTagListWithSlice(
//                        keyword, pageable);
//
//        return EmployerPostSearchWithSliceResponseDto.of(
//                employerPostSlice.stream().map(employerPost -> {
//                    List<String> tagNameList = getTagList(employerPost);
//
//                    return EmployerPostSearchResponseDto.of(employerPost, tagNameList);
//                }).collect(Collectors.toList()), employerPostSlice.hasNext());

        Page<Long> employerPostIdList =
                employerPostRepository.findEmployerPostIdWithPage(keyword,pageable);

        List<EmployerPostKeyWordSearchDBResponseDto> employerPostList =
                employerPostRepository.findEmployerPostKeyWordSearchDB(employerPostIdList.getContent(),pageable.getSort());

        return EmployerPostSearchWithCountResponseDto.of(employerPostIdList.getTotalElements(),
                employerPostList.stream().map(employerPostKeyWordSearchDBResponseDto -> {

                            return EmployerPostSearchResponseDto.ofSearch(employerPostKeyWordSearchDBResponseDto,
                                    getTagListByEmployerPostWorkField(
                                            employerPostKeyWordSearchDBResponseDto));
                        }
                ).collect(Collectors.toList()),employerPostIdList.getTotalPages());
    }


    public EmployerPostSearchWithSliceResponseDto searchMyEmployerPost(final String loginId,final Pageable pageable){

        Member member = getLoginMember(loginId);

        Slice<EmployerPost> employerPostSlice =
                employerPostRepository.findEmployerPostByMemberId(member.getId(),pageable);

        return EmployerPostSearchWithSliceResponseDto.of(employerPostSlice.getContent().stream().map(employerPost -> {

            List<String> tagNameList = getTagList(employerPost);

            return EmployerPostSearchResponseDto.of(employerPost,tagNameList);
        }).collect(Collectors.toList()), employerPostSlice.hasNext());
    }

    public EmployerPostSearchWithSliceResponseDto searchMyBookMarkEmployerPost(final String loginId,final Pageable pageable){

        Member member = getLoginMember(loginId);

        Slice<EmployerPost> bookMarkEmployerPostSlice =
                bookMarkEmployerPostRepository.showMyBookMarkEmployerPost(member.getId(),pageable).map(bookMarkEmployerPost -> bookMarkEmployerPost.getEmployerPost());

        return EmployerPostSearchWithSliceResponseDto.of(bookMarkEmployerPostSlice.getContent().stream().map(employerPost -> {

            List<String> tagNameList = getTagList(employerPost);

            return EmployerPostSearchResponseDto.of(employerPost,tagNameList);
        }).collect(Collectors.toList()), bookMarkEmployerPostSlice.hasNext());
    }


    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.CANT_FIND_MEMBER));
    }

    private  List<String> getTagList(final EmployerPost employerPost) {
        List<String> tagNameList = new ArrayList<>();

        if(employerPost.getBasicPostContent().getWorkFieldTag()!=null){
            tagNameList.add(employerPost.getBasicPostContent().getWorkFieldTag().getName());
        }

        employerPost.getEmployerPostWorkFieldChildTagList().forEach(employerPostWorkFieldChildTag -> {
            tagNameList.add(employerPostWorkFieldChildTag.getWorkFieldChildTag().getName());
        });
        return tagNameList;
    }

    private List<String> getTagListWithFetchTag(final EmployerSearchDBResponseDto employerSearchDBResponseDto){

        List<String> tagNameList = new ArrayList<>();

        if(employerSearchDBResponseDto.getWorkFieldTag()!=null){
            tagNameList.add(employerSearchDBResponseDto.getWorkFieldTag().getName());
        }

        employerSearchDBResponseDto.getEmployerPostWorkFieldChildTagSearchDBResponseDtoList().forEach(employerPostWorkFieldChildTagSearchResponseDto -> {
            tagNameList.add(employerPostWorkFieldChildTagSearchResponseDto.getChildTagName());
        });


        return tagNameList;

    }

    private List<String> getTagListByEmployerPostWorkField(final
    EmployerPostKeyWordSearchDBResponseDto employerPostKeyWordSearchDBResponseDto){

        List<String> tagNameList = new ArrayList<>();

        if(employerPostKeyWordSearchDBResponseDto.getWorkFieldTag()!=null){
            tagNameList.add(employerPostKeyWordSearchDBResponseDto.getWorkFieldTag().getName());
        }

        employerPostWorkFieldChildTagRepository.findByEmployerPost_Id(employerPostKeyWordSearchDBResponseDto.getEmployerPostId())
                .forEach(employerPostWorkFieldChildTag -> tagNameList.add(employerPostWorkFieldChildTag.getWorkFieldChildTag().getName()));

        return tagNameList;

    }


}
