package com.gaduationproject.cre8.app.community.service;

import com.gaduationproject.cre8.app.community.dto.request.CommunityPostEditListRequestDto;
import com.gaduationproject.cre8.app.community.dto.request.CommunityPostEditRequestDto;
import com.gaduationproject.cre8.app.community.dto.request.CommunityPostSaveRequestDto;
import com.gaduationproject.cre8.app.community.dto.response.CommunityPostResponseDto;
import com.gaduationproject.cre8.app.community.dto.response.ReplyListResponseDto;
import com.gaduationproject.cre8.app.community.dto.response.ReplyResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.request.EditEmployeePostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.request.SaveEmployeePostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.SubCategoryWithChildTagResponseDto;
import com.gaduationproject.cre8.app.event.s3.S3UploadImageCommitEvent;
import com.gaduationproject.cre8.app.event.s3.S3UploadImageListCommitEvent;
import com.gaduationproject.cre8.app.event.s3.S3UploadImageListRollbackEvent;
import com.gaduationproject.cre8.app.event.s3.S3UploadImageRollbackEvent;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.community.entity.CommunityBoard;
import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
import com.gaduationproject.cre8.domain.community.entity.CommunityPostImage;
import com.gaduationproject.cre8.domain.community.entity.Reply;
import com.gaduationproject.cre8.domain.community.repository.CommunityBoardRepository;
import com.gaduationproject.cre8.domain.community.repository.CommunityPostImageRepository;
import com.gaduationproject.cre8.domain.community.repository.CommunityPostRepository;
import com.gaduationproject.cre8.domain.community.repository.LikeCommunityPostRepository;
import com.gaduationproject.cre8.domain.community.repository.ReplyRepository;
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
    private final ReplyRepository replyRepository;
    private final CommunityPostImageRepository communityPostImageRepository;

    @Transactional
    public void saveCommunityPost(final String loginId,final CommunityPostSaveRequestDto communityPostSaveRequestDto) throws InterruptedException{



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

        List<ReplyListResponseDto> replyListResponseDtoList = getReplyResponseDtoList(communityPostId);

        return CommunityPostResponseDto.of(communityPost,
                likeCommunityPostRepository.getLikeCountsByCommunityPostId(communityPostId),
                isLikeCommunityPost(loginId,communityPostId),replyListResponseDtoList);

    }

    private List<ReplyListResponseDto> getReplyResponseDtoList(Long communityPostId) {
        List<Reply> parentReply = replyRepository.findParentReplyByPostIdOrderByCreatedWithFetchWriter(
                communityPostId);

        List<ReplyListResponseDto> replyListResponseDtoList = new ArrayList<>();

        //부모-> 자식 순으로 DTO 순서 저장.
        for(Reply parent: parentReply){

            List<ReplyResponseDto> replyChildResponseDto=
                    replyRepository.findChildByParentIdOrderByCreatedAtWithFetchWriter(parent.getId()).stream().map(ReplyResponseDto::of)
                            .collect(Collectors.toList());

            replyListResponseDtoList.add(ReplyListResponseDto.of(ReplyResponseDto.of(parent),replyChildResponseDto));

        }

        return replyListResponseDtoList;
    }

    @Transactional
    public void updateCommunityPost(final String loginId, final CommunityPostEditRequestDto communityPostEditRequestDto) throws InterruptedException{


        CommunityPost communityPost = findCommunityPostById(communityPostEditRequestDto.getCommunityPostId());

        //자신 소유의 CommunityPost 인지 확인
        checkAccessMember(loginId,communityPost);



        //기존의 Image 이벤트 세팅(성공정 commit 시) 및 새로운 이미지, 이벤트 생성(RollBack 시)
        settingS3CommitEventOnOldAccessUrl(communityPost.getAccessUrl());
        String newImageUrl = getAccessUrlWithSettingS3RollBackEvent(communityPostEditRequestDto.getMultipartFile());


        communityPost.changeTitleAndContentsAndAccessUrl(communityPostEditRequestDto.getTitle(),
                                                         communityPostEditRequestDto.getContents(),
                                                         newImageUrl);


    }

    @Transactional
    public void updateCommunityPost2(final String loginId, final CommunityPostEditRequestDto communityPostEditRequestDto,
                                     final String newAccessUrl){


        CommunityPost communityPost = findCommunityPostById(communityPostEditRequestDto.getCommunityPostId());

        settingS3CommitEventOnOldAccessUrl(communityPost.getAccessUrl());
        //자신 소유의 CommunityPost 인지 확인
        checkAccessMember(loginId,communityPost);


        communityPost.changeTitleAndContentsAndAccessUrl(communityPostEditRequestDto.getTitle(),
                communityPostEditRequestDto.getContents(),
                newAccessUrl);


    }

    @Transactional
    public void updateCommunityPostList(final String loginId, final CommunityPostEditListRequestDto communityPostEditListRequestDto) throws InterruptedException{

        List<String> newAccessUrlList = new ArrayList<>();

        //  long l = System.currentTimeMillis();
        // System.out.println("현재시간:" +l);
        communityPostEditListRequestDto.getMultipartFileList().forEach(multipartFile -> {
            newAccessUrlList.add(s3ImageService.saveImage(multipartFile,COMMUNITY_POST_IMAGE,multipartFile.getOriginalFilename()));
        });
        //Thread.sleep(500);
        // System.out.println("차이: "+  (System.currentTimeMillis()-l));


        eventPublisher.publishEvent(
                S3UploadImageListRollbackEvent.builder().newAccessImageUrlList(newAccessUrlList).build());

        CommunityPost communityPost = findCommunityPostById(communityPostEditListRequestDto.getCommunityPostId());

        //자신 소유의 CommunityPost 인지 확인
        checkAccessMember(loginId,communityPost);



        //기존의 Image 이벤트 세팅(성공정 commit 시) 및 새로운 이미지, 이벤트 생성(RollBack 시)
        List<String> deleteImageList = communityPostImageRepository.findByCommunityPost(communityPost).stream()
                .map(CommunityPostImage::getAccessUrl).collect(
                        Collectors.toList());

        settingS3CommitEventOnOldAccessUrlList(deleteImageList,communityPost);

        for(int i=0;i<communityPostEditListRequestDto.getMultipartFileList().size();i++){

            communityPostImageRepository.save(CommunityPostImage.builder().communityPost(communityPost)
                    .accessUrl(newAccessUrlList.get(i)).originalName(communityPostEditListRequestDto.getMultipartFileList().get(i).getOriginalFilename())
                    .build());

        }



        communityPost.changeTitleAndContents(communityPostEditListRequestDto.getTitle(),
                communityPostEditListRequestDto.getContents());



    }

    @Transactional
    public void updateCommunityPostListTest(final String loginId, final CommunityPostEditListRequestDto communityPostEditListRequestDto, final List<String> newAccessUrlList) throws InterruptedException{


        CommunityPost communityPost = findCommunityPostById(communityPostEditListRequestDto.getCommunityPostId());

        //자신 소유의 CommunityPost 인지 확인
        checkAccessMember(loginId,communityPost);



        //기존의 Image 이벤트 세팅(성공정 commit 시) 및 새로운 이미지, 이벤트 생성(RollBack 시)
        List<String> deleteImageList = communityPostImageRepository.findByCommunityPost(communityPost).stream()
                .map(CommunityPostImage::getAccessUrl).collect(
                        Collectors.toList());

        settingS3CommitEventOnOldAccessUrlList(deleteImageList,communityPost);

        for(int i=0;i<communityPostEditListRequestDto.getMultipartFileList().size();i++){

            communityPostImageRepository.save(CommunityPostImage.builder().communityPost(communityPost)
                    .accessUrl(newAccessUrlList.get(i)).originalName(communityPostEditListRequestDto.getMultipartFileList().get(i).getOriginalFilename())
                    .build());

        }



        communityPost.changeTitleAndContents(communityPostEditListRequestDto.getTitle(),
                communityPostEditListRequestDto.getContents());



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

        replyRepository.deleteByCommunityPostId(communityPostId);

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
            throw new BadRequestException(ErrorCode.CANT_ACCESS_COMMUNITY_POST);
        }

    }

    // dto 의 사진이 null 일 경우 기존의 url 반환 , 그렇지 않으면 새로 생성 후 저장
    private String getAccessUrlWithSettingS3RollBackEvent(MultipartFile multipartFile) throws InterruptedException{

        if(checkInputMultiPartFileNull(multipartFile)){
            return null;
        }

        String newImageAccessUrl = s3ImageService.saveImage(multipartFile,COMMUNITY_POST_IMAGE,multipartFile.getOriginalFilename());
        //Thread.sleep(500);
        eventPublisher.publishEvent(
                S3UploadImageRollbackEvent.builder().newAccessImageUrl(newImageAccessUrl).build());

        return newImageAccessUrl;
    }

    private List<String> getAccessUrlListWithSettingS3RollBackEvent(CommunityPost communityPost,List<MultipartFile> multipartFileList) throws InterruptedException{

        List<String> newImageAccessUrlList = new ArrayList<>();

        multipartFileList.forEach(multipartFile -> {
            String newAccessUrl = s3ImageService.saveImage(multipartFile,COMMUNITY_POST_IMAGE,multipartFile.getOriginalFilename());
            communityPostImageRepository.save(CommunityPostImage.builder().communityPost(communityPost).originalName(multipartFile.getOriginalFilename())
                    .accessUrl(newAccessUrl).build());
            newImageAccessUrlList.add(newAccessUrl);

        });

        eventPublisher.publishEvent(
                S3UploadImageListRollbackEvent.builder().newAccessImageUrlList(newImageAccessUrlList).build());

        return newImageAccessUrlList;
    }


    //트랜잭션 커밋시 원활 히 삭제 될 수 있도록 event 를 세팅한다.
    private void settingS3CommitEventOnOldAccessUrl(final String oldAccessImageUrl){

        if(oldAccessImageUrl!=null){
            eventPublisher.publishEvent(
                    S3UploadImageCommitEvent.builder().oldAccessImageUrl(oldAccessImageUrl).build());
        }

    }

    private void settingS3CommitEventOnOldAccessUrlList(final List<String> oldAccessImageUrlList,CommunityPost communityPost){

        if(oldAccessImageUrlList==null || oldAccessImageUrlList.size()==0){
            return;
        }

        communityPostImageRepository.deleteByCommunityPost(communityPost);

        eventPublisher.publishEvent(S3UploadImageListCommitEvent
                .builder().deleteAccessImageUrlList(oldAccessImageUrlList).build());


    }


    //받은 Multipart 값이 null  혹은 empty 인지 판단. 아니라면 false
    private boolean checkInputMultiPartFileNull(MultipartFile multipartFile){
        if(multipartFile==null || multipartFile.isEmpty()){
            return true;
        }

        return false;
    }


}
