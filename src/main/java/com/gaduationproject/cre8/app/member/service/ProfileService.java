package com.gaduationproject.cre8.app.member.service;

import com.gaduationproject.cre8.app.member.dto.S3UploadCommitEvent;
import com.gaduationproject.cre8.app.member.dto.S3UploadRollbackEvent;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.member.editor.MemberEditor;
import com.gaduationproject.cre8.externalApi.s3.S3ImageService;
import com.gaduationproject.cre8.app.member.dto.ProfileWithUserInfoEditRequestDto;

import com.gaduationproject.cre8.app.member.dto.ProfileWithUserInfoResponseDto;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final MemberRepository memberRepository;
    private final S3ImageService s3ImageService;
    private final static String MEMBER_PROFILE_IMAGE="member-images/";
    private final static String DEFAULT_PROFILE_URL="";
    private final ApplicationEventPublisher eventPublisher;


    //memberId 로 그 멤버의 profile 조회

    public ProfileWithUserInfoResponseDto showProfile(final Long memberId){

        Member member = findMemberById(memberId);

        return ProfileWithUserInfoResponseDto.of(member);
    }


    // 내 프로필 수정
    @Transactional
    public void changeMemberProfile(final String loginId , final ProfileWithUserInfoEditRequestDto profileWithUserInfoEditRequestDto){

        Member member = getLoginMember(loginId);

        if(memberRepository.existsByNickName(profileWithUserInfoEditRequestDto.getUserNickName())
                && !member.getNickName().equals(profileWithUserInfoEditRequestDto.getUserNickName())){
            throw new BadRequestException(ErrorCode.DUPLICATE_NICKNAME);
        }

        //기존 member 의 기존 url
        String beforeAccessUrl = member.getAccessUrl();

        //multipart 파일이 비거나 null 인 경우 기존 memberURL 반환 , 그렇지 않으면 새로 생성 후 반환
        String accessUrl = getAccessUrl(profileWithUserInfoEditRequestDto.getMultipartFile(), member);

        MemberEditor.MemberEditorBuilder memberEditorBuilder = member.toEditor();

        MemberEditor memberEditor = memberEditorBuilder.youtubeLink(profileWithUserInfoEditRequestDto.getYoutubeLink())
                        .personalLink(profileWithUserInfoEditRequestDto.getPersonalLink())
                                .twitterLink(profileWithUserInfoEditRequestDto.getTwitterLink())
                                        .personalStatement(
                                                profileWithUserInfoEditRequestDto.getPersonalStatement())
                                                .nickName(
                                                        profileWithUserInfoEditRequestDto.getUserNickName())
                .accessUrl(accessUrl)
                .build();

        member.edit(memberEditor);




        //들어온 값이 빈값이 아닐 때
        if(!accessUrl.equals(beforeAccessUrl)){

            //성공적으로 커밋 시 -> S3 에서 기존 URL 삭제.
            eventPublisher.publishEvent(S3UploadCommitEvent.builder().oldAccessUrl(beforeAccessUrl).build());

            //롤백 시  -> S3 에서 새로 저장한 url 삭제
            eventPublisher.publishEvent(S3UploadRollbackEvent.builder().newAccessUrl(accessUrl).build());
        }



    }


    //멤버 프로필이 기본 url 인 "" 인지 판단.
    private boolean checkDefaultProfileUrl(String accessUrl){
        if(accessUrl.equals(DEFAULT_PROFILE_URL)){
            return true;
        }

        return false;
    }

    // dto 의 사진이 null 일 경우 기존의 url 반환 , 그렇지 않으면 새로 생성 후 저장
    private String getAccessUrl(MultipartFile multipartFile,Member member){

        if(checkInputMultiPartFileNull(multipartFile)){
            return member.getAccessUrl();
        }

        return s3ImageService.saveImage(multipartFile,MEMBER_PROFILE_IMAGE,
                multipartFile.getOriginalFilename());
    }


    //받은 Multipart 값이 null  혹은 empty 인지 판단. 아니라면 false
    private boolean checkInputMultiPartFileNull(MultipartFile multipartFile){
        if(multipartFile==null || multipartFile.isEmpty()){
            return true;
        }

        return false;
    }

    private Member getLoginMember(final String loginId){
        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.LOGIN_ID_NOT_MATCH));
    }
    private Member findMemberById(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(()->new NotFoundException(
                ErrorCode.CANT_FIND_MEMBER
        ));
    }




    //유저 닉네임을 통해 pk 를 얻어 오기
    public Long findPkFromNickName(final String loginId){

        Member member = memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_MEMBER));

        return member.getId();
    }





}
