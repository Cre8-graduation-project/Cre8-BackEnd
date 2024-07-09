package com.gaduationproject.cre8.employmentpost.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.employmentpost.domain.entity.EmployeePost;
import com.gaduationproject.cre8.employmentpost.domain.entity.EmployeePostWorkFieldChildTag;
import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPost;
import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPostWorkFieldChildTag;
import com.gaduationproject.cre8.employmentpost.domain.type.EnrollDurationType;
import com.gaduationproject.cre8.employmentpost.domain.type.PaymentMethod;
import com.gaduationproject.cre8.employmentpost.dto.request.SaveEmployeePostRequestDto;
import com.gaduationproject.cre8.employmentpost.dto.request.SaveEmployerPostRequestDto;
import com.gaduationproject.cre8.employmentpost.dto.response.EmployeePostResponseDto;
import com.gaduationproject.cre8.employmentpost.dto.response.EmployerPostResponseDto;
import com.gaduationproject.cre8.employmentpost.repository.EmployeePostRepository;
import com.gaduationproject.cre8.employmentpost.repository.EmployeePostWorkFieldChildTagRepository;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.repository.MemberRepository;
import com.gaduationproject.cre8.portfolio.service.PortfolioService;
import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldTag;
import com.gaduationproject.cre8.workfieldtag.repository.WorkFieldChildTagRepository;
import com.gaduationproject.cre8.workfieldtag.repository.WorkFieldTagRepository;
import java.util.ArrayList;
import java.util.List;
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

        List<String> tagList = new ArrayList<>();

        if(employeePost.getBasicPostContent().getWorkFieldTag()!=null){
            tagList.add(employeePost.getBasicPostContent().getWorkFieldTag().getName());
        }

        employeePost.getEmployeePostWorkFieldChildTagList().stream().forEach(employeePostWorkFieldChildTag -> {
            tagList.add(employeePostWorkFieldChildTag.getWorkFieldChildTag().getName());
        });

        return EmployeePostResponseDto.of(employeePost,tagList,portfolioService.showPortfolioList(employeePost.getBasicPostContent().getMember()
                .getId()));

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

}