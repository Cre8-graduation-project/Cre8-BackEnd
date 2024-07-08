package com.gaduationproject.cre8.member.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.common.s3.S3ImageService;
import com.gaduationproject.cre8.member.dto.ProfileWithUserInfoEditRequestDto;

import com.gaduationproject.cre8.member.dto.ProfileWithUserInfoResponseDto;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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


    //memberId 로 그 멤버의 profile 조회

    public ProfileWithUserInfoResponseDto showProfile(final Long memberId){

        Member member = findMemberById(memberId);

        return ProfileWithUserInfoResponseDto.of(member);
    }


    // 내 프로필 수정
    @Transactional
    public void changeMemberProfile(final String loginId , final ProfileWithUserInfoEditRequestDto profileWithUserInfoEditRequestDto){

        Member member = getLoginMember(loginId);

        if(memberRepository.existsByNickName(profileWithUserInfoEditRequestDto.getUserNickName())){
            throw new BadRequestException(ErrorCode.DUPLICATE_NICKNAME);
        }

        member.changeProfile(profileWithUserInfoEditRequestDto.getYoutubeLink(),
                profileWithUserInfoEditRequestDto.getPersonalLink(), profileWithUserInfoEditRequestDto.getTwitterLink(),
                profileWithUserInfoEditRequestDto.getPersonalStatement(),
                profileWithUserInfoEditRequestDto.getUserNickName());

        changeMemberImage(profileWithUserInfoEditRequestDto, member);

    }

    private void changeMemberImage(ProfileWithUserInfoEditRequestDto profileWithUserInfoEditRequestDto,
            Member member) {

        if(!checkDefaultProfileUrl(member.getAccessUrl()) && !checkInputMultiPartFileNull(
                profileWithUserInfoEditRequestDto.getMultipartFile())){
            s3ImageService.deleteImage(member.getAccessUrl());
        }

        String accessUrl = getAccessUrl(profileWithUserInfoEditRequestDto.getMultipartFile(),member);

        member.changeAccessUrl(accessUrl);
    }

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





}
