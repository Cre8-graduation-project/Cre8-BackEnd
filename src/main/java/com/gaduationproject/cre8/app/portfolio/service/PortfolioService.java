package com.gaduationproject.cre8.app.portfolio.service;

import com.gaduationproject.cre8.app.portfolio.dto.S3UploadPortfolioImageCommitEvent;
import com.gaduationproject.cre8.app.portfolio.dto.S3UploadPortfolioImageRollbackEvent;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.BadRequestException;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.externalApi.s3.S3ImageService;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.app.portfolio.dto.request.PortfolioEditRequestDto;
import com.gaduationproject.cre8.app.portfolio.dto.response.PortfolioResponseDto;
import com.gaduationproject.cre8.app.portfolio.dto.response.PortfolioSimpleResponseDto;
import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioImage;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioWorkFieldChildTag;
import com.gaduationproject.cre8.domain.portfolio.repository.PortfolioImageRepository;
import com.gaduationproject.cre8.domain.portfolio.repository.PortfolioRepository;
import com.gaduationproject.cre8.domain.portfolio.repository.PortfolioWorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldTag;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldChildTagRepository;
import com.gaduationproject.cre8.domain.workfieldtag.repository.WorkFieldTagRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final MemberRepository memberRepository;
    private final WorkFieldChildTagRepository workFieldChildTagRepository;
    private final WorkFieldTagRepository workFieldTagRepository;
    private final PortfolioWorkFieldChildTagRepository portfolioWorkFieldChildTagRepository;
    private final PortfolioImageRepository portfolioImageRepository;
    private final S3ImageService s3ImageService;
    private final static String portFolioImage = "portfolio-images/";
    private final ApplicationEventPublisher eventPublisher;



    @Transactional
    public Long savePortfolio(final String loginId){

        Member member = getLoginMember(loginId);

        Portfolio portfolio = Portfolio.builder()
                .member(member)
                .build();

        portfolioRepository.save(portfolio);

        return portfolio.getId();

    }

    @Transactional
    public void updatePortfolio(final String loginId, final PortfolioEditRequestDto portfolioEditRequestDto){


        Portfolio portfolio = findPortfolioById(portfolioEditRequestDto.getPortfolioId());

        checkAccessMember(loginId,portfolio);

        WorkFieldTag workFieldTag = getWorkFieldTag(portfolioEditRequestDto.getWorkFieldId());

        List<WorkFieldChildTag> workFieldChildTagList =  getWorkFieldChildTag(
                portfolioEditRequestDto);


        //workFieldTag Description 변경
        portfolio.changeWorkFieldTagAndDescription(workFieldTag, portfolioEditRequestDto.getDescription());


        // Child tag 변경
        portfolioWorkFieldChildTagRepository.deleteByPortfolio(portfolio);

        workFieldChildTagList.forEach(workFieldChildTag -> {

            PortfolioWorkFieldChildTag portfolioWorkFieldChildTag = PortfolioWorkFieldChildTag.builder()
                    .workFieldChildTag(workFieldChildTag)
                    .portfolio(portfolio)
                    .build();

            portfolioWorkFieldChildTagRepository.save(portfolioWorkFieldChildTag);

        });


        //Image 변경
        updatePortfolioImage(portfolioEditRequestDto.getMultipartFileList(),portfolio,portfolioEditRequestDto.getDeletePortfolioImageList());


    }

    public PortfolioResponseDto showPortfolio(final Long portfolioId){

        Portfolio portfolio = portfolioRepository.findByPortfolioIdWithFetchImageAndWorkFieldTag(portfolioId)
                .orElseThrow(()-> new NotFoundException(ErrorCode.CANT_FIND_PORTFOLIO));


        List<String> tagList = new ArrayList<>();

        if(portfolio.getWorkFieldTag()!=null){
            tagList.add(portfolio.getWorkFieldTag().getName());
        }

        portfolioWorkFieldChildTagRepository.findByPortfolioIdWithFetchWorkFieldChildTag(portfolioId).stream().forEach(portfolioWorkFieldChildTag -> {
            tagList.add(portfolioWorkFieldChildTag.getWorkFieldChildTag().getName());
        });


        return PortfolioResponseDto.from(tagList,portfolio);

    }

    public List<PortfolioSimpleResponseDto> showPortfolioList(final Long memberId){

        checkValidMemberId(memberId);


        return portfolioRepository.findByMemberIdWithFetchPortfolioImage(memberId).stream().map(PortfolioSimpleResponseDto::from).collect(
                Collectors.toList());

    }

    @Transactional
    public void deletePortfolio(final String loginId, final Long portfolioId){

        Portfolio portfolio = findPortfolioById(portfolioId);

        checkAccessMember(loginId,portfolio);

        deletePortfolioImage(portfolio);

        portfolioWorkFieldChildTagRepository.deleteByPortfolio(portfolio);

        portfolioRepository.delete(portfolio);

    }







    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(
                ErrorCode.LOGIN_ID_NOT_MATCH));
    }

    //ChildTag 와 Tag
    private List<WorkFieldChildTag> getWorkFieldChildTag(final PortfolioEditRequestDto portfolioEditRequestDto){

        if(portfolioEditRequestDto.getWorkFieldChildTagId()==null){
            return List.of();
        }

        return portfolioEditRequestDto.getWorkFieldChildTagId().stream().map(childId->{

            WorkFieldChildTag workFieldChildTag = workFieldChildTagRepository.findById(childId)
                    .orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_WORK_FIELD_CHILD_TAG));

            if(portfolioEditRequestDto.getWorkFieldId()==null ||
                    workFieldChildTag.getWorkFieldSubCategory().getWorkFieldTag().getId()!= portfolioEditRequestDto.getWorkFieldId()){

                throw new BadRequestException(ErrorCode.NOT_CORRECT_PARENT_TAG);
            }

            return workFieldChildTag;

        }).collect(Collectors.toList());

    }

    private WorkFieldTag getWorkFieldTag(final Long workFieldTagId){

        if(workFieldTagId==null){
            return null;
        }

        return workFieldTagRepository.findById(workFieldTagId).orElseThrow(()-> new NotFoundException(
                ErrorCode.CANT_FIND_WORK_FILED_TAG));
    }

    // 포트폴리오 이미지 update -> listener 사용
    private void updatePortfolioImage(final List<MultipartFile> multipartFileList,final Portfolio portfolio,final List<Long> deletePortfolioImageId){


        List<String> deleteAccessUrlList = new ArrayList<>();
        List<String> newAccessUrlList = new ArrayList<>();


        if(deletePortfolioImageId!=null){

            deletePortfolioImageId.forEach(portfolioImageId->{
                deleteAccessUrlList.add(portfolioImageRepository.findById(portfolioImageId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_PORTFOLIO_IMAGE_ID)).getAccessUrl());
                portfolioImageRepository.deleteById(portfolioImageId);
            });

        }


        if(multipartFileList!=null){

            multipartFileList.stream().forEach(multipartFile -> {

                String accessUrl = s3ImageService.saveImage(multipartFile,portFolioImage,multipartFile.getOriginalFilename());
                newAccessUrlList.add(accessUrl);

                PortfolioImage portfolioImage = PortfolioImage.builder()
                        .originalName(multipartFile.getOriginalFilename())
                        .portfolio(portfolio)
                        .accessUrl(accessUrl)
                        .build();

                portfolio.getPortfolioImageList().add(portfolioImage);

            });
        }


        eventPublisher.publishEvent(S3UploadPortfolioImageRollbackEvent.builder().newAccessUrlList(newAccessUrlList).build());
        eventPublisher.publishEvent(S3UploadPortfolioImageCommitEvent.builder().deleteAccessUrlList(deleteAccessUrlList).build());

    }

    private void deletePortfolioImage(final Portfolio portfolio){

        List<String> deleteAccessUrl = new ArrayList<>();

        portfolioImageRepository.findByPortfolio(portfolio).forEach(portfolioImage -> {
            deleteAccessUrl.add(portfolioImage.getAccessUrl());
        });

        eventPublisher.publishEvent(S3UploadPortfolioImageCommitEvent.builder().deleteAccessUrlList(deleteAccessUrl).build());

        portfolio.getPortfolioImageList().clear();

    }



    private void checkValidMemberId(final Long memberId){

        memberRepository.findById(memberId).orElseThrow(()-> new NotFoundException(ErrorCode.CANT_FIND_MEMBER));
    }

    private void checkAccessMember(final String loginId, final Portfolio portfolio){

        if(!portfolio.getMember().getLoginId().equals(loginId)){
            throw new BadRequestException(ErrorCode.CANT_ACCESS_PORTFOLIO);
        }
    }

    private Portfolio findPortfolioById(final Long portfolioId){

        return portfolioRepository.findById(portfolioId).orElseThrow(()-> new NotFoundException(ErrorCode.CANT_FIND_PORTFOLIO));
    }


}
