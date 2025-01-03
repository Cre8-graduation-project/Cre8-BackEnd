package com.cre8.portfolio.service;


import com.cre8.employmentpost.entity.EmployeePost;
import com.cre8.employmentpost.repository.EmployeePostRepository;
import com.cre8.event.s3.S3UploadImageListRollbackEvent;
import com.cre8.event.s3.UploadImageListCommitDeleteEvent;
import com.cre8.event.s3.UploadImageListCommitSaveEvent;
import com.cre8.member.entity.Member;
import com.cre8.member.repository.MemberRepository;
import com.cre8.portfolio.dto.event.ImageDeleteEventDto;
import com.cre8.portfolio.dto.event.ImageSaveEventDto;
import com.cre8.portfolio.dto.request.PortfolioEditRequestDto;
import com.cre8.portfolio.dto.response.PortfolioResponseDto;
import com.cre8.portfolio.dto.response.PortfolioSimpleResponseDto;
import com.cre8.portfolio.entity.Portfolio;
import com.cre8.portfolio.entity.PortfolioImage;
import com.cre8.portfolio.entity.PortfolioWorkFieldChildTag;
import com.cre8.portfolio.repository.PortfolioImageRepository;
import com.cre8.portfolio.repository.PortfolioRepository;
import com.cre8.portfolio.repository.PortfolioWorkFieldChildTagRepository;
import com.cre8.response.error.ErrorCode;
import com.cre8.response.error.exception.BadRequestException;
import com.cre8.response.error.exception.NotFoundException;
import com.cre8.s3.S3ImageService;
import com.cre8.workfieldtag.entity.WorkFieldChildTag;
import com.cre8.workfieldtag.entity.WorkFieldTag;
import com.cre8.workfieldtag.repository.WorkFieldChildTagRepository;
import com.cre8.workfieldtag.repository.WorkFieldTagRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final EmployeePostRepository employeePostRepository;



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

        Optional<EmployeePost> recentEmployeePostOptional = employeePostRepository.findTop1ByBasicPostContent_Member_IdOrderByIdDesc(
                portfolio.getMember().getId());

        Long recentEmployeePostId = recentEmployeePostOptional.map(EmployeePost::getId).orElse(null);

        return PortfolioResponseDto.from(tagList,portfolio,recentEmployeePostId);

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


        List<ImageDeleteEventDto> imageDeleteEventDtos = new ArrayList<>();
        List<String> newAccessUrlList = new ArrayList<>();
        List<ImageSaveEventDto> imageSaveEventDtos = new ArrayList<>();


        if(deletePortfolioImageId!=null){

            deletePortfolioImageId.forEach(portfolioImageId->{
                imageDeleteEventDtos.add(
                        new ImageDeleteEventDto(portfolioImageId, portfolioImageRepository.findById(portfolioImageId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_PORTFOLIO_IMAGE_ID)).getAccessUrl()));
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

                portfolioImageRepository.save(portfolioImage);
                imageSaveEventDtos.add(new ImageSaveEventDto(portfolioImage.getId()));

            });

        }


        eventPublisher.publishEvent(
                S3UploadImageListRollbackEvent.builder().newAccessImageUrlList(newAccessUrlList).build());
        eventPublisher.publishEvent(UploadImageListCommitDeleteEvent.builder().imageDeleteEventDtos(imageDeleteEventDtos).build());
        eventPublisher.publishEvent(
                UploadImageListCommitSaveEvent.builder().imageSaveEventDtos(imageSaveEventDtos).build());

    }

    private void deletePortfolioImage(final Portfolio portfolio){

        List<ImageDeleteEventDto> imageDeleteEventDtos = new ArrayList<>();

        portfolioImageRepository.findByPortfolio(portfolio).forEach(portfolioImage -> {
            imageDeleteEventDtos.add(new ImageDeleteEventDto(portfolioImage.getId(),portfolioImage.getAccessUrl()));
        });

        eventPublisher.publishEvent(UploadImageListCommitDeleteEvent.builder().imageDeleteEventDtos(imageDeleteEventDtos).build());

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
