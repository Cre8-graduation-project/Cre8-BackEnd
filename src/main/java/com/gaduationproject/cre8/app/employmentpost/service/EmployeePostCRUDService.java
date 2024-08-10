package com.gaduationproject.cre8.app.employmentpost.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePostWorkFieldChildTag;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
    public void saveEmployeePost(final String loginId,final SaveEmployeePostRequestDto saveEmployeePostRequestDto){

        Member member = getLoginMember(loginId);
        WorkFieldTag workFieldTag = getWorkFieldTag(saveEmployeePostRequestDto.getWorkFieldId());
        List<WorkFieldChildTag> workFieldChildTagList = getWorkFieldChildTag(
                saveEmployeePostRequestDto.getWorkFieldChildTagId(),
                saveEmployeePostRequestDto.getWorkFieldId());

        EmployeePost employeePost = EmployeePost.builder()
                .member(member)
                .title(saveEmployeePostRequestDto.getTitle())
                .workFieldTag(workFieldTag)
                .paymentMethod(PaymentMethod.toPaymentMethodEnum(saveEmployeePostRequestDto.getPaymentMethod()))
                .paymentAmount(saveEmployeePostRequestDto.getPaymentAmount())
                .careerYear(saveEmployeePostRequestDto.getCareerYear())
                .contents(saveEmployeePostRequestDto.getContents())
                .contact(saveEmployeePostRequestDto.getContact())
                .build();

        employeePostRepository.save(employeePost);

        workFieldChildTagList.forEach(workFieldChildTag -> {

            EmployeePostWorkFieldChildTag employeePostWorkFieldChildTag = EmployeePostWorkFieldChildTag.builder()
                    .employeePost(employeePost)
                    .workFieldChildTag(workFieldChildTag)
                    .build();

            employeePostWorkFieldChildTagRepository.save(employeePostWorkFieldChildTag);

        });


    }

    public EmployeePostResponseDto showEmployeePost(final Long employeePostId){

        EmployeePost employeePost = employeePostRepository
                .findByIdWithFetchMemberAndWorkFieldTagAndEmployeePostChildTagListAndWorkFieldChildTag(employeePostId).orElseThrow(
                        ()-> new NotFoundException(ErrorCode.CANT_FIND_EMPLOYEE_POST));

        Map<String,List<String>> childTagMap = new LinkedHashMap<>();

        employeePost.getEmployeePostWorkFieldChildTagList()
                .stream().forEach(employeePostWorkFieldChildTag -> {

                    String subCategoryName = employeePostWorkFieldChildTag.getWorkFieldChildTag().getWorkFieldSubCategory().getName();
                    String childTagName = employeePostWorkFieldChildTag.getWorkFieldChildTag().getName();

                    childTagMap.computeIfAbsent(subCategoryName, categoryName -> new ArrayList<>()).add(childTagName);

                });

        List<SubCategoryWithChildTagResponseDto> subCategoryWithChildTagResponseDtoList = childTagMap.keySet().stream().map(subCategoryName ->{
            return SubCategoryWithChildTagResponseDto.of(subCategoryName,childTagMap.get(subCategoryName));
        }).collect(Collectors.toList());

        Long ownerMemberId = employeePost.getBasicPostContent().getMember().getId();

        return EmployeePostResponseDto.of(subCategoryWithChildTagResponseDtoList
                                          ,employeePost
                                          ,portfolioService.showPortfolioList(ownerMemberId),
                                           ownerMemberId);

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


        employeePost.changeAllExceptMemberAndId(editEmployeePostRequestDto.getTitle(), workFieldTag,PaymentMethod.toPaymentMethodEnum(editEmployeePostRequestDto.getPaymentMethod()),
                editEmployeePostRequestDto.getPaymentAmount(),
                editEmployeePostRequestDto.getCareerYear(), editEmployeePostRequestDto.getContents(),editEmployeePostRequestDto.getContact());

    }

    @Transactional
    public void deleteEmployeePost(final String loginId,final Long employeePostId){

        EmployeePost employeePost = findEmployeePostById(employeePostId);
        checkAccessMember(loginId,employeePost);

        employeePostWorkFieldChildTagRepository.deleteByEmployeePost(employeePost);

        employeePostRepository.deleteById(employeePostId);

    }

    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.LOGIN_ID_NOT_MATCH));
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

}
