package com.gaduationproject.cre8.employmentpost.service;


import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPost;
import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPostWorkFieldChildTag;
import com.gaduationproject.cre8.employmentpost.domain.type.EnrollDurationType;
import com.gaduationproject.cre8.employmentpost.domain.type.PaymentMethod;
import com.gaduationproject.cre8.employmentpost.dto.request.EditEmployerPostRequestDto;
import com.gaduationproject.cre8.employmentpost.dto.request.SaveEmployerPostRequestDto;
import com.gaduationproject.cre8.employmentpost.dto.response.EmployerPostResponseDto;
import com.gaduationproject.cre8.employmentpost.dto.response.SubCategoryWithChildTagResponseDto;
import com.gaduationproject.cre8.employmentpost.repository.EmployerPostRepository;
import com.gaduationproject.cre8.employmentpost.repository.EmployerPostWorkFieldChildTagRepository;
import com.gaduationproject.cre8.member.entity.Member;
import com.gaduationproject.cre8.member.repository.MemberRepository;
import com.gaduationproject.cre8.portfolio.entity.PortfolioWorkFieldChildTag;
import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.workfieldtag.entity.WorkFieldTag;
import com.gaduationproject.cre8.workfieldtag.repository.WorkFieldChildTagRepository;
import com.gaduationproject.cre8.workfieldtag.repository.WorkFieldTagRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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
public class EmployerPostCRUDService {

    private final EmployerPostRepository employerPostRepository;
    private final MemberRepository memberRepository;
    private final WorkFieldTagRepository workFieldTagRepository;
    private final WorkFieldChildTagRepository workFieldChildTagRepository;
    private final EmployerPostWorkFieldChildTagRepository employerPostWorkFieldChildTagRepository;


    //Employer Post 저장
    @Transactional
    public void saveEmployerPost(final String loginId,final SaveEmployerPostRequestDto saveEmployerPostRequestDto){

        checkDeadLineOnlyOnDeadLine(saveEmployerPostRequestDto.getDeadLine(),EnrollDurationType.toEnrollDurationTypeEnum(
                saveEmployerPostRequestDto.getEnrollDurationType()));

        Member member = getLoginMember(loginId);
        WorkFieldTag workFieldTag = getWorkFieldTag(saveEmployerPostRequestDto.getWorkFieldId());
        List<WorkFieldChildTag> workFieldChildTagList = getWorkFieldChildTag(
                saveEmployerPostRequestDto.getWorkFieldChildTagId(),
                saveEmployerPostRequestDto.getWorkFieldId());

        EmployerPost employerPost = EmployerPost.builder()
                .member(member)
                .title(saveEmployerPostRequestDto.getTitle())
                .workFieldTag(workFieldTag)
                .paymentMethod(PaymentMethod.toPaymentMethodEnum(saveEmployerPostRequestDto.getPaymentMethod()))
                .paymentAmount(saveEmployerPostRequestDto.getPaymentAmount())
                .companyName(saveEmployerPostRequestDto.getCompanyName())
                .numberOfEmployee(saveEmployerPostRequestDto.getNumberOfEmployee())
                .enrollDurationType(EnrollDurationType.toEnrollDurationTypeEnum(
                        saveEmployerPostRequestDto.getEnrollDurationType()))
                .deadLine(saveEmployerPostRequestDto.getDeadLine())
                .hopeCareerYear(saveEmployerPostRequestDto.getHopeCareerYear())
                .contents(saveEmployerPostRequestDto.getContents())
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
    public EmployerPostResponseDto showEmployerPost(final Long employerPostId){

        EmployerPost employerPost = employerPostRepository
                .findByIdWithFetchWorkFieldTagAndEmployerPostChildTagListAndWorkFieldChildTag(employerPostId).orElseThrow(
                        ()-> new NotFoundException(ErrorCode.CANT_FIND_EMPLOYER_POST));

        Map<String,List<String>> childTagMap = new LinkedHashMap<>();

        employerPost.getEmployerPostWorkFieldChildTagList()
                .stream().forEach(employerPostWorkFieldChildTag -> {

            String subCategoryName = employerPostWorkFieldChildTag.getWorkFieldChildTag().getWorkFieldSubCategory().getName();
            String childTagName = employerPostWorkFieldChildTag.getWorkFieldChildTag().getName();

            childTagMap.computeIfAbsent(subCategoryName, categoryName -> new ArrayList<>()).add(childTagName);

        });

        List<SubCategoryWithChildTagResponseDto> subCategoryWithChildTagResponseDtoList = childTagMap.keySet().stream().map(subCategoryName ->{
            return SubCategoryWithChildTagResponseDto.of(subCategoryName,childTagMap.get(subCategoryName));
        }).collect(Collectors.toList());


        return EmployerPostResponseDto.of(subCategoryWithChildTagResponseDtoList,employerPost);

    }

    @Transactional
    public void updateEmployerPost(final String loginId, final EditEmployerPostRequestDto editEmployerPostRequestDto){

        checkDeadLineOnlyOnDeadLine(editEmployerPostRequestDto.getDeadLine(),EnrollDurationType.toEnrollDurationTypeEnum(
                editEmployerPostRequestDto.getEnrollDurationType()));

        EmployerPost employerPost = findEmployerPostById(editEmployerPostRequestDto.getEmployerPostId());
        checkAccessMember(loginId,employerPost);
        WorkFieldTag workFieldTag = getWorkFieldTag(editEmployerPostRequestDto.getWorkFieldId());
        List<WorkFieldChildTag> workFieldChildTagList = getWorkFieldChildTag(editEmployerPostRequestDto.getWorkFieldChildTagId(),
                editEmployerPostRequestDto.getWorkFieldId());


        employerPostWorkFieldChildTagRepository.deleteByEmployerPost(employerPost);
        workFieldChildTagList.forEach(workFieldChildTag -> {

            EmployerPostWorkFieldChildTag employerPostWorkFieldChildTag = EmployerPostWorkFieldChildTag.builder()
                    .employerPost(employerPost)
                    .workFieldChildTag(workFieldChildTag)
                    .build();

            employerPostWorkFieldChildTagRepository.save(employerPostWorkFieldChildTag);

        });


        employerPost.changeAllExceptMemberAndId(editEmployerPostRequestDto.getTitle(), workFieldTag,PaymentMethod.toPaymentMethodEnum(editEmployerPostRequestDto.getPaymentMethod()),
                editEmployerPostRequestDto.getPaymentAmount(),editEmployerPostRequestDto.getCompanyName(),
                editEmployerPostRequestDto.getNumberOfEmployee(),
                EnrollDurationType.toEnrollDurationTypeEnum(editEmployerPostRequestDto.getEnrollDurationType()),
                        editEmployerPostRequestDto.getDeadLine(),
                        editEmployerPostRequestDto.getHopeCareerYear(),editEmployerPostRequestDto.getContents()
                        );

    }

    @Transactional
    public void deleteEmployerPost(final String loginId,final Long employerPostId){

        EmployerPost employerPost = findEmployerPostById(employerPostId);
        checkAccessMember(loginId,employerPost);

        employerPostWorkFieldChildTagRepository.deleteByEmployerPost(employerPost);

        employerPostRepository.deleteById(employerPostId);

    }





    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.LOGIN_ID_NOT_MATCH));
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




}
