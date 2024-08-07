package com.gaduationproject.cre8.domain.chat.repository;

import com.gaduationproject.cre8.domain.chat.entity.ChattingRoom;
import com.gaduationproject.cre8.domain.chat.entity.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message,Long> {

    List<Message> findByChattingRoom(ChattingRoom chattingRoom);

    @Query("select m from Message m where m.chattingRoom=:chattingRoom order by m.createdAt desc limit 1")
    Optional<Message> findLatestMessageByChattingRoom(@Param("chattingRoom") ChattingRoom chattingRoom);

}
