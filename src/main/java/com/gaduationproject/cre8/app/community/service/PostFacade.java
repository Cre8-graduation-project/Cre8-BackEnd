package com.gaduationproject.cre8.app.community.service;

import com.gaduationproject.cre8.app.community.dto.request.CommunityPostEditListRequestDto;
import com.gaduationproject.cre8.app.community.dto.request.CommunityPostEditRequestDto;
import com.gaduationproject.cre8.app.event.s3.S3UploadImageCommitEvent;
import com.gaduationproject.cre8.app.event.s3.S3UploadImageListRollbackEvent;
import com.gaduationproject.cre8.app.event.s3.S3UploadImageRollbackEvent;
import com.gaduationproject.cre8.externalApi.s3.S3ImageService;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostFacade {

    private final S3ImageService s3ImageService;
    private final CommunityPostCRUDService communityPostCRUDService;
    private static final String COMMUNITY_POST_IMAGE="communityPost-images/";
    private final ApplicationEventPublisher eventPublisher;
    private final DataSource dataSource;

    public void updatePostImageAndUpdate(final String loginId, final CommunityPostEditRequestDto communityPostEditRequestDto) throws InterruptedException{

        printConnectionStatus();

       // long l = System.currentTimeMillis();
      //  System.out.println("현재시간:" +l);
        String newAccessUrl = s3ImageService.saveImage(communityPostEditRequestDto.getMultipartFile(),COMMUNITY_POST_IMAGE,
                communityPostEditRequestDto.getMultipartFile().getOriginalFilename());
       // System.out.println("차이: "+  (System.currentTimeMillis()-l));

        eventPublisher.publishEvent(S3UploadImageRollbackEvent.builder().newAccessImageUrl(newAccessUrl).build());

        communityPostCRUDService.updateCommunityPost2(loginId,communityPostEditRequestDto,newAccessUrl);
    }

    public void updatePostImageListAndUpdate(final String loginId, final CommunityPostEditListRequestDto communityPostEditListRequestDto) throws InterruptedException{

        printConnectionStatus();


        List<String> newAccessUrlList = new ArrayList<>();

        long l = System.currentTimeMillis();
        System.out.println("현재시간:" +l);
        communityPostEditListRequestDto.getMultipartFileList().forEach(multipartFile -> {
            newAccessUrlList.add(s3ImageService.saveImage(multipartFile,COMMUNITY_POST_IMAGE,multipartFile.getOriginalFilename()));
        });

        System.out.println("차이: "+  (System.currentTimeMillis()-l));


        eventPublisher.publishEvent(
                S3UploadImageListRollbackEvent.builder().newAccessImageUrlList(newAccessUrlList).build());

        communityPostCRUDService.updateCommunityPostListTest(loginId,communityPostEditListRequestDto,newAccessUrlList);
    }

    private void printConnectionStatus() {
        final HikariPoolMXBean hikariPoolMXBean = ((HikariDataSource) dataSource).getHikariPoolMXBean();
        System.out.println("################################");
        System.out.println("현재 active인 connection의 수 : " + hikariPoolMXBean.getActiveConnections());
        System.out.println("현재 idle인 connection의 수 : " + hikariPoolMXBean.getIdleConnections());
        System.out.println("################################");
    }


}
