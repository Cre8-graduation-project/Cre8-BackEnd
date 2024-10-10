package com.gaduationproject.cre8.externalApi.mongodb.domain;

import com.gaduationproject.cre8.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
