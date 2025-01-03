package com.cre8.redis.repository;


import com.cre8.redis.domain.ChattingRoomConnect;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;

public interface ChattingRoomConnectRepository extends CrudRepository<ChattingRoomConnect, String> {

    Set<ChattingRoomConnect> findByChattingRoomId(Long chattingRoomId);

    Optional<ChattingRoomConnect> findByChattingRoomIdAndLoginId(final Long chattingRoomId,final String loginId);

    Optional<ChattingRoomConnect> findBySessionId(final String sessionId);
}

