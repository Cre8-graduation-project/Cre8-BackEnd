package com.gaduationproject.cre8.app.notify.service;

import com.gaduationproject.cre8.app.notify.dto.NotifyDto;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
import com.gaduationproject.cre8.externalApi.mongodb.domain.NotificationType;
import com.gaduationproject.cre8.externalApi.mongodb.domain.Notify;
import com.gaduationproject.cre8.domain.notify.repository.EmitterRepository;
import com.gaduationproject.cre8.externalApi.mongodb.repository.NotifyRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotifyService {

    private final EmitterRepository emitterRepository;
    private final NotifyRepository notifyRepository;
    private final MemberRepository memberRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    public SseEmitter subscribe(String username, String lastEventId) {

        Member member = memberRepository.findMemberByLoginId(username).orElseThrow(()->new NotFoundException(
                ErrorCode.CANT_FIND_MEMBER));

        String emitterId = makeTimeIncludeId(member.getId());

        log.info("lastEventId = {}",lastEventId);
        log.info("emitterId = {}",emitterId);

        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(member.getId());
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userEmail=" + username + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        sendUnreadNotify(lastEventId,member.getId(),emitterId,emitter);

        return emitter;
    }

    private String makeTimeIncludeId(Long memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {

            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            );
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendUnreadNotify(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {

        List<Notify> notifyList = notifyRepository.findByMemberIdAndRead(memberId,false);

        notifyList.forEach(notify -> sendNotification(emitter,notify.getEventId(),emitterId,NotifyDto.from(notify)));

    }

    //@Override
    public void send(Member receiver, NotificationType notificationType, String content, String url) {

        Long receiverId = receiver.getId();
        String eventId = makeTimeIncludeId(receiverId);

        Notify notification = notifyRepository.save(createNotification(eventId,receiver, notificationType, content, url));

        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
        emitters.forEach(
                (emitterId, emitter) -> {

                    sendNotification(emitter, eventId, emitterId, NotifyDto.from(notification));
                }
        );
    }

    private Notify createNotification(final String eventId,final Member receiver,
            final NotificationType notificationType,final String contents,final String url) {

        return Notify.builder()
                .eventId(eventId)
                .url(url)
                .notificationType(notificationType)
                .read(false)
                .memberId(receiver.getId())
                .contents(contents)
                .build();
    }


}
