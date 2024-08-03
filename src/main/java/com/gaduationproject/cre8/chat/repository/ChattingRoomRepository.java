package com.gaduationproject.cre8.chat.repository;

import com.gaduationproject.cre8.chat.entity.ChattingRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom,Long> {

    @Query("select cr from ChattingRoom cr where (cr.receiver.id=:receiverId and cr.sender.id=:senderId) or (cr.receiver.id=:senderId and cr.sender.id=:receiverId)")
    Optional<ChattingRoom> findByParticipant(@Param("receiverId") Long receiverId,@Param("senderId") Long senderId);

}
