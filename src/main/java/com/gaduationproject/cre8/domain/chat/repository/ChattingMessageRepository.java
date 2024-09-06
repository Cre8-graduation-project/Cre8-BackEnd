package com.gaduationproject.cre8.domain.chat.repository;

import com.gaduationproject.cre8.domain.chat.entity.ChattingMessage;
import com.gaduationproject.cre8.domain.chat.entity.ChattingRoom;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingMessageRepository extends MongoRepository<ChattingMessage,String> {


    List<ChattingMessage> findByChattingRoomId(final Long chattingRoomId);

    Optional<ChattingMessage> findTop1ByChattingRoomIdOrderByCreatedAtDesc(final Long chattingRoomId);
}
