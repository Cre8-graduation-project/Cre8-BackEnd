package com.gaduationproject.cre8.app.employmentpost.service;


import com.gaduationproject.cre8.app.event.s3.S3UploadImageCommitEvent;
import com.gaduationproject.cre8.app.event.s3.S3UploadImageRollbackEvent;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPostWorkFieldChildTag;
import com.gaduationproject.cre8.domain.employmentpost.repository.BookMarkEmployerPostRepository;
import com.gaduationproject.cre8.domain.employmentpost.type.EnrollDurationType;
import com.gaduationproject.cre8.domain.employmentpost.type.PaymentMethod;
import com.gaduationproject.cre8.app.employmentpost.dto.request.EditEmployerPostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.request.SaveEmployerPostRequestDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployerPostResponseDto;
import com.gaduationproject.cre8.app.employmentpost.dto.response.SubCategoryWithChildTagResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployerPostRepository;
import com.gaduationproject.cre8.domain.employmentpost.repository.EmployerPostWorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldTagRepository;
import com.gaduationproject.cre8.externalApi.s3.S3ImageService;
import java.time.LocalDate;
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
public class EmployerPostCRUDService {

    private final EmployerPostRepository employerPostRepository;
    private final MemberRepository memberRepository;
    private final WorkFieldTagRepository workFieldTagRepository;
    private final WorkFieldChildTagRepository workFieldChildTagRepository;
    private final EmployerPostWorkFieldChildTagRepository employerPostWorkFieldChildTagRepository;
    private final S3ImageService s3ImageService;
    private final ApplicationEventPublisher eventPublisher;
    private final static String EMPLOYER_POST_IMAGE="employerPost-images/";
    private final BookMarkEmployerPostRepository bookMarkEmployerPostRepository;



    //Employer Post 저장
    @Transactional
    public void saveEmployerPost(final String loginId,final SaveEmployerPostRequestDto saveEmployerPostRequestDto){

        //DeadLine 인 경우 날짜가 입력되는지 확인
        checkDeadLineOnlyOnDeadLine(saveEmployerPostRequestDto.getDeadLine(),EnrollDurationType.toEnrollDurationTypeEnum(
                saveEmployerPostRequestDto.getEnrollDurationType()));

        Member member = getLoginMember(loginId);

        //작업 필드 체크 및 리스트 가져오기
        WorkFieldTag workFieldTag = getWorkFieldTag(saveEmployerPostRequestDto.getWorkFieldId());
        List<WorkFieldChildTag> workFieldChildTagList = getWorkFieldChildTag(
                saveEmployerPostRequestDto.getWorkFieldChildTagId(),
                saveEmployerPostRequestDto.getWorkFieldId());

        //이벤트 생성 및 이미지 생성
        String accessImageUrl = getAccessUrlWithSettingS3RollBackEvent(saveEmployerPostRequestDto.getMultipartFile());

        EmployerPost employerPost = EmployerPost.builder()
                .member(member)
                .title(saveEmployerPostRequestDto.getTitle())
                .workFieldTag(workFieldTag)
                .paymentMethod(PaymentMethod.toPaymentMethodEnum(saveEmployerPostRequestDto.getPaymentMethod()))
                .paymentAmount(saveEmployerPostRequestDto.getPaymentAmount())
                .companyName(saveEmployerPostRequestDto.getCompanyName())
                .numberOfEmployee(saveEmployerPostRequestDto.getNumberOfEmployee())
                .enrollDurationType(EnrollDurationType.toEnrollDurationTypeEnum(saveEmployerPostRequestDto.getEnrollDurationType()))
                .deadLine(saveEmployerPostRequestDto.getDeadLine())
                .hopeCareerYear(saveEmployerPostRequestDto.getHopeCareerYear())
                .contents(saveEmployerPostRequestDto.getContents())
                .contact(saveEmployerPostRequestDto.getContact())
                .accessUrl(accessImageUrl)
                .build();

        employerPostRepository.save(employerPost);

        workFieldChildTagList.forEach(workFieldChildTag -> {

            EmployerPostWorkFieldChildTag employerPostWorkFieldChildTag = EmployerPostWorkFieldChildTag.builder()
                    .employerPost(employerPost)
                    .workFieldChildTag(workFieldChildTag)
                    .build();

            employerPostWorkFieldChildTagRepository.save(employerPostWorkFieldChildTag);

        });


    }

    //Employer Post 단건 조회
    public EmployerPostResponseDto showEmployerPost(final Long employerPostId,final String loginId){

        EmployerPost employerPost = employerPostRepository
                .findByIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag(employerPostId).orElseThrow(
                        ()-> new NotFoundException(ErrorCode.CANT_FIND_EMPLOYER_POST));

        List<SubCategoryWithChildTagResponseDto> subCategoryWithChildTagResponseDtoList = getSubCategoryWithChildTagResponseDtoList(
                employerPost);



        return EmployerPostResponseDto.of(subCategoryWithChildTagResponseDtoList,employerPost,isBookMarkedEmployerPost(loginId,employerPostId));

    }

    private  List<SubCategoryWithChildTagResponseDto> getSubCategoryWithChildTagResponseDtoList(
            EmployerPost employerPost) {

        Map<String,List<String>> childTagMap = new LinkedHashMap<>();

        employerPost.getEmployerPostWorkFieldChildTagList()
                .stream().forEach(employerPostWorkFieldChildTag -> {

            String subCategoryName = employerPostWorkFieldChildTag.getWorkFieldChildTag().getWorkFieldSubCategory().getName();
            String childTagName = employerPostWorkFieldChildTag.getWorkFieldChildTag().getName();

            childTagMap.computeIfAbsent(subCategoryName, categoryName -> new ArrayList<>()).add(childTagName);

        });

        List<SubCategoryWithChildTagResponseDto> subCategoryWithChildTagResponseDtoList = childTagMap.keySet().stream().map(subCategoryName ->
             SubCategoryWithChildTagResponseDto.of(subCategoryName,childTagMap.get(subCategoryName))
        ).collect(Collectors.toList());


        return subCategoryWithChildTagResponseDtoList;
    }

    @Transactional
    public void updateEmployerPost(final String loginId, final EditEmployerPostRequestDto editEmployerPostRequestDto){

        //데드라인인 경우만 날짜가 입력되는지 확인
        checkDeadLineOnlyOnDeadLine(editEmployerPostRequestDto.getDeadLine(),EnrollDurationType.toEnrollDurationTypeEnum(
                editEmployerPostRequestDto.getEnrollDurationType()));

        EmployerPost employerPost = findEmployerPostById(editEmployerPostRequestDto.getEmployerPostId());
        checkAccessMember(loginId,employerPost);

        //작업태그 확인 및 리스트 가져오기
        WorkFieldTag workFieldTag = getWorkFieldTag(editEmployerPostRequestDto.getWorkFieldId());
        List<WorkFieldChildTag> workFieldChildTagList = getWorkFieldChildTag(editEmployerPostRequestDto.getWorkFieldChildTagId(),
                editEmployerPostRequestDto.getWorkFieldId());

        //작업 태그 업데이트
        employerPostWorkFieldChildTagRepository.deleteByEmployerPost(employerPost);
        workFieldChildTagList.forEach(workFieldChildTag -> {

            EmployerPostWorkFieldChildTag employerPostWorkFieldChildTag = EmployerPostWorkFieldChildTag.builder()
                    .employerPost(employerPost)
                    .workFieldChildTag(workFieldChildTag)
                    .build();

            employerPostWorkFieldChildTagRepository.save(employerPostWorkFieldChildTag);

        });

        //기존의 Image 이벤트 세팅(성공정 commit 시) 및 새로운 이미지, 이벤트 생성(RollBack 시)
        settingS3CommitEventOnOldAccessUrl(employerPost.getBasicPostContent().getAccessUrl());
        String newImageUrl = getAccessUrlWithSettingS3RollBackEvent(editEmployerPostRequestDto.getMultipartFile());


        employerPost.changeAllExceptMemberAndId(editEmployerPostRequestDto.getTitle(), workFieldTag,PaymentMethod.toPaymentMethodEnum(editEmployerPostRequestDto.getPaymentMethod()),
                editEmployerPostRequestDto.getPaymentAmount(),editEmployerPostRequestDto.getCompanyName(),
                editEmployerPostRequestDto.getNumberOfEmployee(),
                EnrollDurationType.toEnrollDurationTypeEnum(editEmployerPostRequestDto.getEnrollDurationType()),
                        editEmployerPostRequestDto.getDeadLine(),
                        editEmployerPostRequestDto.getHopeCareerYear(),editEmployerPostRequestDto.getContents(),
                editEmployerPostRequestDto.getContact(),newImageUrl);


    }

    @Transactional
    public void deleteEmployerPost(final String loginId,final Long employerPostId){

        EmployerPost employerPost = findEmployerPostById(employerPostId);

        checkAccessMember(loginId,employerPost);
        settingS3CommitEventOnOldAccessUrl(employerPost.getBasicPostContent().getAccessUrl());

        employerPostWorkFieldChildTagRepository.deleteByEmployerPost(employerPost);

        bookMarkEmployerPostRepository.deleteByEmployerPostId(employerPostId);

        employerPostRepository.deleteById(employerPostId);


    }





    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.LOGIN_ID_NOT_MATCH));
    }

    private boolean isBookMarkedEmployerPost(final String loginId,final Long employerPostId){

        if(loginId ==null){
            return false;
        }

        Member member = getLoginMember(loginId);

        return bookMarkEmployerPostRepository.existsByMemberIdAndEmployerPostId(member.getId(),employerPostId);

    }

    private EmployerPost findEmployerPostById(final Long employerPostId){

        return employerPostRepository.findById(employerPostId).orElseThrow(()-> new NotFoundException(ErrorCode.CANT_FIND_EMPLOYER_POST));

    }

    private void checkAccessMember(final String loginId,final EmployerPost employerPost){

        if(loginId==null || !loginId.equals(employerPost.getBasicPostContent().getMember().getLoginId())){
            throw new BadRequestException(ErrorCode.CANT_ACCESS_EMPLOYER_POST);
        }

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

    private void checkDeadLineOnlyOnDeadLine(final LocalDate deadLine,final EnrollDurationType enrollDurationType){

        if(enrollDurationType!=EnrollDurationType.DEAD_LINE && deadLine!=null){
            throw new BadRequestException(ErrorCode.CANT_SET_DEADLINE_WITH_NO_ENUM_DEADLINE);
        }

        if(enrollDurationType==EnrollDurationType.DEAD_LINE && deadLine ==null){
            throw new BadRequestException(ErrorCode.INSERT_DEADLINE_ON_ENUM_DEADLINE);
        }


    }

    // dto 의 사진이 null 일 경우 기존의 url 반환 , 그렇지 않으면 새로 생성 후 저장
    private String getAccessUrlWithSettingS3RollBackEvent(MultipartFile multipartFile){

        if(checkInputMultiPartFileNull(multipartFile)){
            return null;
        }

        String newImageAccessUrl = s3ImageService.saveImage(multipartFile,EMPLOYER_POST_IMAGE,multipartFile.getOriginalFilename());
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
