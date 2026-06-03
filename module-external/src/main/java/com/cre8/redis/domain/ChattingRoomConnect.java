package com.cre8.redis.domain;


import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = {"sessionId"})
@RedisHash(value = "ChattingRoomConnect")
public class ChattingRoomConnect {

    @Id
    private String sessionId;

    @Indexed
    private Long chattingRoomId;

    private String loginId;

    @Builder
    public ChattingRoomConnect(Long chattingRoomId, String loginId, String sessionId) {
        this.chattingRoomId = chattingRoomId;
        this.loginId = loginId;
        this.sessionId = sessionId;
    }

}
