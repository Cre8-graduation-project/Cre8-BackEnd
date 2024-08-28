package com.gaduationproject.cre8.app.employmentpost.service;

import com.gaduationproject.cre8.app.event.s3.S3UploadImageCommitEvent;
import com.gaduationproject.cre8.app.event.s3.S3UploadImageRollbackEvent;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePostWorkFieldChildTag;
import com.gaduationproject.cre8.domain.employmentpost.repository.BookMarkEmployeePostRepository;
import com.gaduationproject.cre8.domain.employmentpost.type.PaymentMethod;
import com.gaduationproject.cre8.app.employmentpost.dto.request.EditEmployeePostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.request.SaveEmployeePostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.SubCategoryWithChildTagResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployeePostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployeePostWorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.app.portfolio.service.PortfolioService;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldTagRepository;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeePostCRUDService {

    private final PortfolioService portfolioService;
    private final EmployeePostRepository employeePostRepository;
    private final MemberRepository memberRepository;
    private final WorkFieldTagRepository workFieldTagRepository;
    private final WorkFieldChildTagRepository workFieldChildTagRepository;
    private final EmployeePostWorkFieldChildTagRepository employeePostWorkFieldChildTagRepository;
    private final S3ImageService s3ImageService;
    private final ApplicationEventPublisher eventPublisher;
    private final static String EMPLOYEE_POST_IMAGE="employeePost-images/";
    private final BookMarkEmployeePostRepository bookMarkEmployeePostRepository;


    @Transactional
    public void saveEmployeePost(final String loginId,final SaveEmployeePostRequestDto saveEmployeePostRequestDto){

        Member member = getLoginMember(loginId);

        //작업 태그 관련 체크 및 List 생성
        WorkFieldTag workFieldTag = getWorkFieldTag(saveEmployeePostRequestDto.getWorkFieldId());

        List<WorkFieldChildTag> workFieldChildTagList = getWorkFieldChildTag(
                saveEmployeePostRequestDto.getWorkFieldChildTagId(),
                saveEmployeePostRequestDto.getWorkFieldId());

        //post 배경 이미지 생성 및 추후 RollBack 을 위한 TransactionEvent 세팅
        String accessUrl = getAccessUrlWithSettingS3RollBackEvent(saveEmployeePostRequestDto.getMultipartFile());


        // EmployeePost 저장
        EmployeePost employeePost = EmployeePost.builder()
                .member(member)
                .title(saveEmployeePostRequestDto.getTitle())
                .workFieldTag(workFieldTag)
                .paymentMethod(PaymentMethod.toPaymentMethodEnum(saveEmployeePostRequestDto.getPaymentMethod()))
                .paymentAmount(saveEmployeePostRequestDto.getPaymentAmount())
                .careerYear(saveEmployeePostRequestDto.getCareerYear())
                .contents(saveEmployeePostRequestDto.getContents())
                .contact(saveEmployeePostRequestDto.getContact())
                .accessUrl(accessUrl)
                .build();

        employeePostRepository.save(employeePost);


        // EmployeePost 관련 작업 태그 저장.
        workFieldChildTagList.forEach(workFieldChildTag -> {

            EmployeePostWorkFieldChildTag employeePostWorkFieldChildTag = EmployeePostWorkFieldChildTag.builder()
                    .employeePost(employeePost)
                    .workFieldChildTag(workFieldChildTag)
                    .build();

            employeePostWorkFieldChildTagRepository.save(employeePostWorkFieldChildTag);

        });



    }

    public EmployeePostResponseDto showEmployeePost(final Long employeePostId,final String loginId){

        EmployeePost employeePost = employeePostRepository
                .findByIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag(employeePostId).orElseThrow(
                        ()-> new NotFoundException(ErrorCode.CANT_FIND_EMPLOYEE_POST));


        List<SubCategoryWithChildTagResponseDto> subCategoryWithChildTagResponseDtoList = getSubCategoryWithChildTagResponseDtoList(
                employeePost);


        Long writerId = employeePost.getBasicPostContent().getMember().getId();


        return EmployeePostResponseDto.of(subCategoryWithChildTagResponseDtoList
                                          ,employeePost
                                          ,portfolioService.showPortfolioList(writerId)
                                          ,isBookMarkedEmployeePost(loginId, employeePostId));

    }

    // SubCategory 와 ChildTag 함께 반환
    private List<SubCategoryWithChildTagResponseDto> getSubCategoryWithChildTagResponseDtoList(
            EmployeePost employeePost) {

        Map<String,List<String>> childTagMap = new LinkedHashMap<>();

        employeePost.getEmployeePostWorkFieldChildTagList()
                .stream().forEach(employeePostWorkFieldChildTag -> {

                    String subCategoryName = employeePostWorkFieldChildTag.getWorkFieldChildTag().getWorkFieldSubCategory().getName();
                    String childTagName = employeePostWorkFieldChildTag.getWorkFieldChildTag().getName();

                    childTagMap.computeIfAbsent(subCategoryName, categoryName -> new ArrayList<>()).add(childTagName);

                });

        List<SubCategoryWithChildTagResponseDto> subCategoryWithChildTagResponseDtoList = childTagMap.keySet().stream().map(subCategoryName ->
             SubCategoryWithChildTagResponseDto.of(subCategoryName,childTagMap.get(subCategoryName))
        ).collect(Collectors.toList());


        return subCategoryWithChildTagResponseDtoList;
    }

    @Transactional
    public void updateEmployeePost(final String loginId, final EditEmployeePostRequestDto editEmployeePostRequestDto){


        EmployeePost employeePost = findEmployeePostById(editEmployeePostRequestDto.getEmployeePostId());

        //자신 소유의 EmployeePost 인지 확인
        checkAccessMember(loginId,employeePost);

        //작업 태그 확인 및 리스트 가져오기
        WorkFieldTag workFieldTag = getWorkFieldTag(editEmployeePostRequestDto.getWorkFieldId());
        List<WorkFieldChildTag> workFieldChildTagList = getWorkFieldChildTag(editEmployeePostRequestDto.getWorkFieldChildTagId(),
                editEmployeePostRequestDto.getWorkFieldId());


        //작업 태그 삭제 후 저장.
        employeePostWorkFieldChildTagRepository.deleteByEmployeePost(employeePost);
        workFieldChildTagList.forEach(workFieldChildTag -> {

            EmployeePostWorkFieldChildTag employeePostWorkFieldChildTag = EmployeePostWorkFieldChildTag.builder()
                    .employeePost(employeePost)
                    .workFieldChildTag(workFieldChildTag)
                    .build();

            employeePostWorkFieldChildTagRepository.save(employeePostWorkFieldChildTag);

        });


        //기존의 Image 이벤트 세팅(성공정 commit 시) 및 새로운 이미지, 이벤트 생성(RollBack 시)
        settingS3CommitEventOnOldAccessUrl(employeePost.getBasicPostContent().getAccessUrl());
        String newImageUrl = getAccessUrlWithSettingS3RollBackEvent(editEmployeePostRequestDto.getMultipartFile());


        employeePost.changeAllExceptMemberAndId(editEmployeePostRequestDto.getTitle(), workFieldTag,PaymentMethod.toPaymentMethodEnum(editEmployeePostRequestDto.getPaymentMethod()),
                editEmployeePostRequestDto.getPaymentAmount(),
                editEmployeePostRequestDto.getCareerYear(), editEmployeePostRequestDto.getContents(),editEmployeePostRequestDto.getContact()
                ,newImageUrl);


    }

    @Transactional
    public void deleteEmployeePost(final String loginId,final Long employeePostId){

        EmployeePost employeePost = findEmployeePostById(employeePostId);


        checkAccessMember(loginId,employeePost);

        //기존의 Image 이벤트 세팅(성공적 commit 시)
        settingS3CommitEventOnOldAccessUrl(employeePost.getBasicPostContent().getAccessUrl());


        //관련 작업 태그 삭제
        employeePostWorkFieldChildTagRepository.deleteByEmployeePost(employeePost);

        //BookMark 삭제
        bookMarkEmployeePostRepository.deleteByEmployeePostId(employeePostId);

        //post 자체 삭제
        employeePostRepository.deleteById(employeePostId);


    }

    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.LOGIN_ID_NOT_MATCH));
    }

    private boolean isBookMarkedEmployeePost(final String loginId,final Long employeePostId){

        if(loginId ==null){
            return false;
        }

        Member member = getLoginMember(loginId);

        return bookMarkEmployeePostRepository.existsByMemberIdAndEmployeePostId(member.getId(),employeePostId);

    }

    private WorkFieldTag getWorkFieldTag(final Long workFieldTagId){

        if(workFieldTagId == null){
            return null;
        }

        return workFieldTagRepository.findById(workFieldTagId).orElseThrow(()-> new NotFoundException(ErrorCode.CANT_FIND_WORK_FILED_TAG));
    }

    private List<WorkFieldChildTag> getWorkFieldChildTag(final List<Long> workFieldChildTagId,final Long workFieldId){

        return workFieldChildTagId.stream().map(childId->{

            WorkFieldChildTag workFieldChildTag = workFieldChildTagRepository.findById(childId)
                    .orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_WORK_FIELD_CHILD_TAG));

            if(workFieldId==null ||
                    workFieldChildTag.getWorkFieldSubCategory().getWorkFieldTag().getId()!= workFieldId){
                throw new BadRequestException(ErrorCode.NOT_CORRECT_PARENT_TAG);
            }

            return workFieldChildTag;

        }).collect(Collectors.toList());

    }

    private EmployeePost findEmployeePostById(final Long employeePostId){
        return employeePostRepository.findById(employeePostId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_EMPLOYEE_POST));
    }

    private void checkAccessMember(final String loginId,final EmployeePost employeePost){

        if(loginId==null || !loginId.equals(employeePost.getBasicPostContent().getMember().getLoginId())){
            throw new BadRequestException(ErrorCode.CANT_ACCESS_EMPLOYEE_POST);
        }

    }

    // dto 의 사진이 null 일 경우 기존의 url 반환 , 그렇지 않으면 새로 생성 후 저장
    private String getAccessUrlWithSettingS3RollBackEvent(MultipartFile multipartFile){

        if(checkInputMultiPartFileNull(multipartFile)){
            return null;
        }

        String newImageAccessUrl = s3ImageService.saveImage(multipartFile,EMPLOYEE_POST_IMAGE,multipartFile.getOriginalFilename());
        eventPublisher.publishEvent(S3UploadImageRollbackEvent.builder().newAccessImageUrl(newImageAccessUrl).build());

        return newImageAccessUrl;
    }


    //트랜잭션 커밋시 원활 히 삭제 될 수 있도록 event 를 세팅한다.
    private void settingS3CommitEventOnOldAccessUrl(final String oldAccessImageUrl){

        if(oldAccessImageUrl!=null){
            eventPublisher.publishEvent(S3UploadImageCommitEvent.builder().oldAccessImageUrl(oldAccessImageUrl).build());
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
