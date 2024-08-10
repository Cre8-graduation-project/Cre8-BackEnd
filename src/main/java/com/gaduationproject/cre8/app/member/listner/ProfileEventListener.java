package com.gaduationproject.cre8.app.member.listner;

import com.gaduationproject.cre8.app.member.dto.S3UploadCommitEvent;
import com.gaduationproject.cre8.app.member.dto.S3UploadRollbackEvent;
import com.gaduationproject.cre8.externalApi.s3.S3ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileEventListener {
    private final S3ImageService s3ImageService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void transactionalEventListenerAfterRollback(final S3UploadRollbackEvent s3UploadRollbackEvent) {
        log.info("TransactionPhase.AFTER_ROLLBACK ---> {}", s3UploadRollbackEvent.getNewAccessUrl());
        s3ImageService.deleteImage(s3UploadRollbackEvent.getNewAccessUrl());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void transactionalEventListenerAfterCommit(final S3UploadCommitEvent s3UploadCommitEvent) {
        log.info("TransactionPhase.AFTER_COMMIT ---> {}", s3UploadCommitEvent.getOldAccessUrl());
        s3ImageService.deleteImage(s3UploadCommitEvent.getOldAccessUrl());
    }

}
