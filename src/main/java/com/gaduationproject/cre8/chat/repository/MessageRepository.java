package com.gaduationproject.cre8.chat.repository;

import com.gaduationproject.cre8.chat.entity.ChattingRoom;
import com.gaduationproject.cre8.chat.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {

    List<Message> findByChattingRoom(ChattingRoom chattingRoom);

}
