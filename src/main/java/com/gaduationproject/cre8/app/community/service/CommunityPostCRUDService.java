package com.gaduationproject.cre8.app.community.service;

import com.gaduationproject.cre8.app.community.dto.request.CommunityPostEditRequestDto;
import com.gaduationproject.cre8.app.community.dto.request.CommunityPostSaveRequestDto;
import com.gaduationproject.cre8.app.community.dto.response.CommunityPostResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.request.EditEmployeePostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.request.SaveEmployeePostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.SubCategoryWithChildTagResponseDto;
import com.gaduationproject.cre8.app.event.s3.S3UploadImageCommitEvent;
import com.gaduationproject.cre8.app.event.s3.S3UploadImageRollbackEvent;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.community.entity.CommunityBoard;
import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
import com.gaduationproject.cre8.domain.community.repository.CommunityBoardRepository;
import com.gaduationproject.cre8.domain.community.repository.CommunityPostRepository;
import com.gaduationproject.cre8.domain.community.repository.LikeCommunityPostRepository;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePostWorkFieldChildTag;
import com.gaduationproject.cre8.domain.employmentpost.type.PaymentMethod;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import com.gaduationproject.cre8.externalApi.s3.S3ImageService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommunityPostCRUDService {

    private final CommunityBoardRepository communityBoardRepository;
    private final CommunityPostRepository communityPostRepository;
    private final MemberRepository memberRepository;
    private final S3ImageService s3ImageService;
    private final ApplicationEventPublisher eventPublisher;
    private static final String COMMUNITY_POST_IMAGE="communityPost-images/";
    private final LikeCommunityPostRepository likeCommunityPostRepository;

    @Transactional
    public void saveCommunityPost(final String loginId,final CommunityPostSaveRequestDto communityPostSaveRequestDto){



        //post 배경 이미지 생성 및 추후 RollBack 을 위한 TransactionEvent 세팅
        String accessUrl = getAccessUrlWithSettingS3RollBackEvent(communityPostSaveRequestDto.getMultipartFile());

        CommunityPost communityPost = CommunityPost.builder()
                .communityBoard(getCommunityBoardById(communityPostSaveRequestDto.getCommunityBoardId()))
                .title(communityPostSaveRequestDto.getTitle())
                .contents(communityPostSaveRequestDto.getContents())
                .writer(getLoginMember(loginId))
                .accessUrl(accessUrl)
                .build();

        communityPostRepository.save(communityPost);


    }

    public CommunityPostResponseDto showCommunityPost(final Long communityPostId,final String loginId){

        CommunityPost communityPost = communityPostRepository
                .findCommunityPostByIdWithFetchWriter(communityPostId).orElseThrow(
                        ()-> new NotFoundException(ErrorCode.CANT_FIND_COMMUNITY_POST));


        return CommunityPostResponseDto.of(communityPost,isLikeCommunityPost(loginId,communityPostId));

    }

    @Transactional
    public void updateCommunityPost(final String loginId, final CommunityPostEditRequestDto communityPostEditRequestDto){


        CommunityPost communityPost = findCommunityPostById(communityPostEditRequestDto.getCommunityPostId());

        //자신 소유의 CommunityPost 인지 확인
        checkAccessMember(loginId,communityPost);



        //기존의 Image 이벤트 세팅(성공정 commit 시) 및 새로운 이미지, 이벤트 생성(RollBack 시)
        settingS3CommitEventOnOldAccessUrl(communityPost.getAccessUrl());
        String newImageUrl = getAccessUrlWithSettingS3RollBackEvent(communityPostEditRequestDto.getMultipartFile());


        communityPost.changeTitleAndContentsAndAccessUrl(communityPostEditRequestDto.getTitle(),
                                                         communityPost.getContents(),
                                                         newImageUrl);


    }

    @Transactional
    public void deleteCommunityPost(final String loginId,final Long communityPostId){

        CommunityPost communityPost = findCommunityPostById(communityPostId);


        checkAccessMember(loginId,communityPost);

        //기존의 Image 이벤트 세팅(성공적 commit 시)
        settingS3CommitEventOnOldAccessUrl(communityPost.getAccessUrl());


        //likePost 삭제
        likeCommunityPostRepository.deleteByCommunityPostId(communityPostId);

        //댓글 삭제


        //post 자체 삭제
        communityPostRepository.deleteById(communityPostId);


    }

    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.LOGIN_ID_NOT_MATCH));
    }

    private boolean isLikeCommunityPost(final String loginId,final Long communityPostId){

        if(loginId ==null){
            return false;
        }

        Member member = getLoginMember(loginId);

        return likeCommunityPostRepository.existsByCommunityPostIdAndLikerId(member.getId(),communityPostId);

    }


    private CommunityBoard getCommunityBoardById(final Long communityBoardId){

        return communityBoardRepository.findById(communityBoardId)
                .orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_COMMUNITY_BOARD));
    }

    private CommunityPost findCommunityPostById(final Long communityPostId){
        return communityPostRepository.findById(communityPostId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_COMMUNITY_POST));
    }

    private void checkAccessMember(final String loginId,final CommunityPost communityPost){

        if(loginId==null || !loginId.equals(communityPost.getWriter().getLoginId())){
            throw new BadRequestException(ErrorCode.CANT_ACCESS_EMPLOYEE_POST);
        }

    }

    // dto 의 사진이 null 일 경우 기존의 url 반환 , 그렇지 않으면 새로 생성 후 저장
    private String getAccessUrlWithSettingS3RollBackEvent(MultipartFile multipartFile){

        if(checkInputMultiPartFileNull(multipartFile)){
            return null;
        }

        String newImageAccessUrl = s3ImageService.saveImage(multipartFile,COMMUNITY_POST_IMAGE,multipartFile.getOriginalFilename());
        eventPublisher.publishEvent(
                S3UploadImageRollbackEvent.builder().newAccessImageUrl(newImageAccessUrl).build());

        return newImageAccessUrl;
    }


    //트랜잭션 커밋시 원활 히 삭제 될 수 있도록 event 를 세팅한다.
    private void settingS3CommitEventOnOldAccessUrl(final String oldAccessImageUrl){

        if(oldAccessImageUrl!=null){
            eventPublisher.publishEvent(
                    S3UploadImageCommitEvent.builder().oldAccessImageUrl(oldAccessImageUrl).build());
        }

    }


    //받은 Multipart 값이 null  혹은 empty 인지 판단. 아니라면 false
    private boolean checkInputMultiPartFileNull(MultipartFile multipartFile){
        if(multipartFile==null || multipartFile.isEmpty()){
            return true;
        }

        return false;
    }


}
