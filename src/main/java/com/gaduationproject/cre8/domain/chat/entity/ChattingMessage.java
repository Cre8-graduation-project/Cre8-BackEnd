package com.gaduationproject.cre8.domain.chat.entity;

import com.gaduationproject.cre8.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chatting") // 실제 몽고 DB 컬렉션 이름
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChattingMessage {

    @Id
    private String id;

    private Long chattingRoomId;

    private Long senderId;

    private String contents;

    @Builder
    public ChattingMessage(final Long chattingRoomId, final Long senderId,final String contents) {

        this.chattingRoomId = chattingRoomId;
        this.senderId = senderId;
        this.contents = contents;
    }

}
