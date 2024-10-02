package com.gaduationproject.cre8.externalApi.redis.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@RedisHash(value = "ChattingRoomConnect")
public class ChattingRoomConnect {

    @Id
    private String id;

    @Indexed
    private Long chattingRoomId;

    @Indexed
    private String loginId;

    @Builder
    public ChattingRoomConnect(Long chattingRoomId, String loginId) {
        this.chattingRoomId = chattingRoomId;
        this.loginId = loginId;
    }
}
