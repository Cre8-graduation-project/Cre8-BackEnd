package com.gaduationproject.cre8.app.employmentpost.service;

import com.gaduationproject.cre8.app.member.dto.S3UploadCommitEvent;
import com.gaduationproject.cre8.app.member.dto.S3UploadRollbackEvent;
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
import org.springframework.security.core.parameters.P;
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

        WorkFieldTag workFieldTag = getWorkFieldTag(saveEmployeePostRequestDto.getWorkFieldId());

        List<WorkFieldChildTag> workFieldChildTagList = getWorkFieldChildTag(
                saveEmployeePostRequestDto.getWorkFieldChildTagId(),
                saveEmployeePostRequestDto.getWorkFieldId());

        String accessUrl = getAccessUrl(saveEmployeePostRequestDto.getMultipartFile());

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

        workFieldChildTagList.forEach(workFieldChildTag -> {

            EmployeePostWorkFieldChildTag employeePostWorkFieldChildTag = EmployeePostWorkFieldChildTag.builder()
                    .employeePost(employeePost)
                    .workFieldChildTag(workFieldChildTag)
                    .build();

            employeePostWorkFieldChildTagRepository.save(employeePostWorkFieldChildTag);

        });


        eventPublisher.publishEvent(S3UploadRollbackEvent.builder().newAccessUrl(accessUrl).build());

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

        checkAccessMember(loginId,employeePost);

        WorkFieldTag workFieldTag = getWorkFieldTag(editEmployeePostRequestDto.getWorkFieldId());
        List<WorkFieldChildTag> workFieldChildTagList = getWorkFieldChildTag(editEmployeePostRequestDto.getWorkFieldChildTagId(),
                editEmployeePostRequestDto.getWorkFieldId());


        employeePostWorkFieldChildTagRepository.deleteByEmployeePost(employeePost);
        workFieldChildTagList.forEach(workFieldChildTag -> {

            EmployeePostWorkFieldChildTag employeePostWorkFieldChildTag = EmployeePostWorkFieldChildTag.builder()
                    .employeePost(employeePost)
                    .workFieldChildTag(workFieldChildTag)
                    .build();

            employeePostWorkFieldChildTagRepository.save(employeePostWorkFieldChildTag);

        });

        String beforeUrl =employeePost.getBasicPostContent().getAccessUrl();
        String accessUrl = getAccessUrl(editEmployeePostRequestDto.getMultipartFile());


        employeePost.changeAllExceptMemberAndId(editEmployeePostRequestDto.getTitle(), workFieldTag,PaymentMethod.toPaymentMethodEnum(editEmployeePostRequestDto.getPaymentMethod()),
                editEmployeePostRequestDto.getPaymentAmount(),
                editEmployeePostRequestDto.getCareerYear(), editEmployeePostRequestDto.getContents(),editEmployeePostRequestDto.getContact()
                ,accessUrl);


        if(beforeUrl!=null){
            eventPublisher.publishEvent(S3UploadCommitEvent.builder().oldAccessUrl(beforeUrl).build());
        }
        eventPublisher.publishEvent(S3UploadRollbackEvent.builder().newAccessUrl(accessUrl).build());



    }

    @Transactional
    public void deleteEmployeePost(final String loginId,final Long employeePostId){

        EmployeePost employeePost = findEmployeePostById(employeePostId);


        checkAccessMember(loginId,employeePost);
        String oldAccessUrl = employeePost.getBasicPostContent().getAccessUrl();

        employeePostWorkFieldChildTagRepository.deleteByEmployeePost(employeePost);

        bookMarkEmployeePostRepository.deleteByEmployeePostId(employeePostId);
        employeePostRepository.deleteById(employeePostId);

        if(oldAccessUrl!=null){
            eventPublisher.publishEvent(S3UploadCommitEvent.builder().oldAccessUrl(oldAccessUrl).build());
        }


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
    private String getAccessUrl(MultipartFile multipartFile){

        if(checkInputMultiPartFileNull(multipartFile)){
            return null;
        }

        return s3ImageService.saveImage(multipartFile,EMPLOYEE_POST_IMAGE,
                multipartFile.getOriginalFilename());
    }


    //받은 Multipart 값이 null  혹은 empty 인지 판단. 아니라면 false
    private boolean checkInputMultiPartFileNull(MultipartFile multipartFile){
        if(multipartFile==null || multipartFile.isEmpty()){
            return true;
        }

        return false;
    }

}
