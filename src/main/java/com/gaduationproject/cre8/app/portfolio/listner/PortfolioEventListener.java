package com.gaduationproject.cre8.app.portfolio.listner;

import com.gaduationproject.cre8.app.member.dto.S3UploadCommitEvent;
import com.gaduationproject.cre8.app.member.dto.S3UploadRollbackEvent;
import com.gaduationproject.cre8.app.portfolio.dto.S3UploadPortfolioImageCommitEvent;
import com.gaduationproject.cre8.app.portfolio.dto.S3UploadPortfolioImageRollbackEvent;
import com.gaduationproject.cre8.externalApi.s3.S3ImageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PortfolioEventListener {

    private final S3ImageService s3ImageService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void transactionalEventListenerAfterRollback(final S3UploadPortfolioImageRollbackEvent s3UploadPortfolioImageRollbackEvent) {

        s3UploadPortfolioImageRollbackEvent.getNewAccessUrlList().forEach(accessUrl->{
            s3ImageService.deleteImage(accessUrl);
        });
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void transactionalEventListenerAfterCommit(final S3UploadPortfolioImageCommitEvent s3UploadPortfolioImageCommitEvent) {

        s3UploadPortfolioImageCommitEvent.getDeleteAccessUrlList().forEach(accessUrl->{
            s3ImageService.deleteImage(accessUrl);
        });
    }

}

