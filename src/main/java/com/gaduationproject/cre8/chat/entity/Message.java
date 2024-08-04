package com.gaduationproject.cre8.chat.entity;

import com.gaduationproject.cre8.common.baseentity.BaseEntity;
import com.gaduationproject.cre8.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_room_id",nullable = false,updatable = false)
    private ChattingRoom chattingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id",nullable = false,updatable = false)
    private Member sender;


    @Column(length = 500,nullable = false,updatable = false)
    private String contents;

    @Builder
    public Message(ChattingRoom chattingRoom, Member sender, String contents) {
        this.chattingRoom = chattingRoom;
        this.sender = sender;
        this.contents = contents;
    }
}
