package com.cre8.mongodb.repository;


import com.cre8.mongodb.domain.Notify;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotifyRepository extends MongoRepository<Notify,String> {

    List<Notify> findByMemberIdAndRead(final Long memberId,boolean read);

    boolean existsByMemberIdAndAndRead(final Long memberId, boolean read);

}
