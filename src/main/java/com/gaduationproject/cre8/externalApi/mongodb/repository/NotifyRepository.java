package com.gaduationproject.cre8.externalApi.mongodb.repository;

import com.gaduationproject.cre8.externalApi.mongodb.domain.ChattingMessage;
import com.gaduationproject.cre8.externalApi.mongodb.domain.NotificationType;
import com.gaduationproject.cre8.externalApi.mongodb.domain.Notify;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotifyRepository extends MongoRepository<Notify,String> {

    List<Notify> findByMemberIdAndRead(final Long memberId,boolean read);

    boolean existsByMemberIdAndAndRead(final Long memberId, boolean read);

}
