package com.gaduationproject.cre8.app.employmentpost.service;

import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchWithSliceResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.TestEmployeePostSearchResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.TestEmployeePostSearchWithCountResponseDto;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchResponseDto2;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchResponseDto3;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePostWorkFieldChildTag;
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

    public TestEmployeePostSearchWithCountResponseDto searchEmployeePostTest(final EmployeePostSearch employerPostSearch,
            final Pageable pageable){

        Page<EmployeeSearchResponseDto> employeePostSearchResponseDtoPage =
                employeePostRepository.testShowEmployeePostListWithPage(employerPostSearch,pageable);

        return TestEmployeePostSearchWithCountResponseDto.of(employeePostSearchResponseDtoPage.getTotalElements(),
                employeePostSearchResponseDtoPage.getContent().stream().map(employeeSearchResponseDto-> {
                    List<String> tagNameList = testGetTagList(employeeSearchResponseDto);

                    return TestEmployeePostSearchResponseDto.of(employeeSearchResponseDto,tagNameList);

                }).collect(
                        Collectors.toList()),employeePostSearchResponseDtoPage.getTotalPages());
    }

    public TestEmployeePostSearchWithCountResponseDto searchEmployeePostTest2(final EmployeePostSearch employerPostSearch,
            final Pageable pageable){

        Page<EmployeeSearchResponseDto2> employeePostSearchResponseDtoPage =
                employeePostRepository.testShowEmployeePostListWithPage2(employerPostSearch,pageable);

        return TestEmployeePostSearchWithCountResponseDto.of(employeePostSearchResponseDtoPage.getTotalElements(),
                employeePostSearchResponseDtoPage.getContent().stream().map(employeeSearchResponseDto-> {
                    List<String> tagNameList = testGetTagList2(employeeSearchResponseDto);

                    return TestEmployeePostSearchResponseDto.of2(employeeSearchResponseDto,tagNameList);

                }).collect(
                        Collectors.toList()),employeePostSearchResponseDtoPage.getTotalPages());
    }

    public TestEmployeePostSearchWithCountResponseDto searchEmployeePostTest3(final EmployeePostSearch employerPostSearch,
            final Pageable pageable){

        Page<EmployeeSearchResponseDto3> employeePostSearchResponseDtoPage =
                employeePostRepository.testShowEmployeePostListWithPage3(employerPostSearch,pageable);

        return TestEmployeePostSearchWithCountResponseDto.of(employeePostSearchResponseDtoPage.getTotalElements(),
                employeePostSearchResponseDtoPage.getContent().stream().map(employeeSearchResponseDto-> {
                    List<String> tagNameList = testGetTagList3(employeeSearchResponseDto);

                    return TestEmployeePostSearchResponseDto.of3(employeeSearchResponseDto,tagNameList);

                }).collect(
                        Collectors.toList()),employeePostSearchResponseDtoPage.getTotalPages());
    }



    public EmployeePostSearchWithSliceResponseDto searchEmployeeByKeyword(final String keyword,final Pageable pageable){

        Slice<EmployeePost> employeePostSlice =
                employeePostRepository.findEmployeePostWithFetchMemberAndWorkFieldTagAndChildTagListWithSlice(keyword,pageable);

        return EmployeePostSearchWithSliceResponseDto.of(employeePostSlice.getContent().stream().map(employeePost -> {
            List<String> tagNameList = getTagList(employeePost);

            return EmployeePostSearchResponseDto.of(employeePost,tagNameList);

        }).collect(Collectors.toList()), employeePostSlice.hasNext());
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

    private List<String> testGetTagList(final EmployeeSearchResponseDto employeeSearchResponseDto){

        List<String> tagNameList = new ArrayList<>();

        if(employeeSearchResponseDto.getWorkFieldTag()!=null){
            tagNameList.add(employeeSearchResponseDto.getWorkFieldTag().getName());
        }

        employeeSearchResponseDto.getEmployeePostWorkFieldChildTagSearchResponseDtoList().forEach(employeePostWorkFieldChildTagSearchResponseDto -> {
            tagNameList.add(employeePostWorkFieldChildTagSearchResponseDto.getChildTagName());
        });

//        employeePostWorkFieldChildTagRepository.findByEmployeePost_IdWithFetchWorkFieldChildTag(employeeSearchResponseDto.getEmployeePostId()).
//                forEach(employeePostWorkFieldChildTag -> {
//                            tagNameList.add(employeePostWorkFieldChildTag.getWorkFieldChildTag().getName());
//                        }
//                );

        return tagNameList;
    }

    private List<String> testGetTagList2(final EmployeeSearchResponseDto2 employeeSearchResponseDto){

        List<String> tagNameList = new ArrayList<>();


        if(employeeSearchResponseDto.getWorkFieldTag()!=null){
            tagNameList.add(employeeSearchResponseDto.getWorkFieldTag().getName());
        }

        employeePostWorkFieldChildTagRepository.findByEmployeePost_IdWithFetchWorkFieldChildTag(employeeSearchResponseDto.getEmployeePostId()).
                forEach(employeePostWorkFieldChildTag -> {
                    tagNameList.add(employeePostWorkFieldChildTag.getWorkFieldChildTag().getName());
                }
        );


        return tagNameList;
    }

    private List<String> testGetTagList3(final EmployeeSearchResponseDto3 employeeSearchResponseDto){

        List<String> tagNameList = new ArrayList<>();


        if(employeeSearchResponseDto.getWorkFieldTag()!=null){
            tagNameList.add(employeeSearchResponseDto.getWorkFieldTag().getName());
        }

        /*
        employeePost.getEmployeePostWorkFieldChildTagList().forEach(employeePostWorkFieldChildTag-> {
            tagNameList.add(employeePostWorkFieldChildTag.getWorkFieldChildTag().getName());
        });
        */
        employeePostWorkFieldChildTagRepository.findByEmployeePost_IdWithFetchWorkFieldChildTag(employeeSearchResponseDto.getEmployeePostId()).
                forEach(employeePostWorkFieldChildTag -> {
                            tagNameList.add(employeePostWorkFieldChildTag.getWorkFieldChildTag().getName());
                        }
                );

        return tagNameList;
    }

}
