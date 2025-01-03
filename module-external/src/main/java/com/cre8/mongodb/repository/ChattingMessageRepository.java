package com.cre8.mongodb.repository;


import com.cre8.mongodb.domain.ChattingMessage;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingMessageRepository extends MongoRepository<ChattingMessage,String> {


    Slice<ChattingMessage> findByChattingRoomId(final Long chattingRoomId,final Pageable pageable);

    Optional<ChattingMessage> findTop1ByChattingRoomIdOrderByCreatedAtDesc(final Long chattingRoomId);
}
