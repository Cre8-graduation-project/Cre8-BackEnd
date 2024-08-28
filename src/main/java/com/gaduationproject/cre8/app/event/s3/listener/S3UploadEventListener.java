package com.gaduationproject.cre8.app.event.s3.listener;

import com.gaduationproject.cre8.app.event.s3.S3UploadImageCommitEvent;
import com.gaduationproject.cre8.app.event.s3.S3UploadImageRollbackEvent;
import com.gaduationproject.cre8.app.event.s3.S3UploadImageListCommitEvent;
import com.gaduationproject.cre8.app.event.s3.S3UploadImageListRollbackEvent;
import com.gaduationproject.cre8.externalApi.s3.S3ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@RequiredArgsConstructor
public class S3UploadEventListener {

    private final S3ImageService s3ImageService;

    //단건으로 이미지 저장하는 로직 중 예외 시 새롭게 저장 된 이미지 롤백
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void transactionalEventListenerAfterRollback(final S3UploadImageRollbackEvent s3UploadImageRollbackEvent) {

        s3ImageService.deleteImage(s3UploadImageRollbackEvent.getNewAccessImageUrl());
    }

    //여러 이미지 저장하는 로직 중 예외 시 새롭게 저장 된 이미지들 롤백
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void transactionalEventListenerAfterRollback(final S3UploadImageListRollbackEvent s3UploadImageListRollbackEvent) {

        s3UploadImageListRollbackEvent.getNewAccessImageUrlList().forEach(accessUrl->{
            s3ImageService.deleteImage(accessUrl);
        });
    }

    //단건으로 이미지 저장하는 로직 중 commit 시 그제서야 이미지 삭제
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void transactionalEventListenerAfterCommit(final S3UploadImageCommitEvent s3UploadImageCommitEvent) {

        s3ImageService.deleteImage(s3UploadImageCommitEvent.getOldAccessImageUrl());
    }



    //여러 건으로 이미지 저장하는 로직 중 commit 시 그제서야 이미지들 삭제
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void transactionalEventListenerAfterCommit(final S3UploadImageListCommitEvent s3UploadImageListCommitEvent) {

        s3UploadImageListCommitEvent.getDeleteAccessImageUrlList().forEach(accessUrl->{
            s3ImageService.deleteImage(accessUrl);
        });
    }

}
