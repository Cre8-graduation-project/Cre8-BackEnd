package com.cre8.mongodb.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notify")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notify {

    @Id
    private String id;

    private Long memberId;

    private String contents;

    private NotificationType notificationType;

    private boolean read;

    private Long postId;




    @Builder
    public Notify(final String contents,boolean read,final NotificationType notificationType,
            final Long  memberId,final Long postId) {
        this.contents = contents;
        this.read = read;
        this.notificationType = notificationType;
        this.memberId = memberId;
        this.postId = postId;
    }
}
