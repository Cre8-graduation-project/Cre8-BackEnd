package com.gaduationproject.cre8.domain.chat.repository;

import com.gaduationproject.cre8.domain.chat.entity.ChattingMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingMessageRepository extends MongoRepository<ChattingMessage,String> {



}
