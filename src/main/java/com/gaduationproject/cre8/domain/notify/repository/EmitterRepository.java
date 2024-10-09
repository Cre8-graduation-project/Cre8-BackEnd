package com.gaduationproject.cre8.domain.notify.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();
    private final Map<String,Object> eventCache = new ConcurrentHashMap<>();

    public SseEmitter save(String emitterId,SseEmitter sseEmitter){
        emitterMap.put(emitterId,sseEmitter);

        return sseEmitter;
    }

    public void saveEventCache(final String eventCacheId,Object event){
        eventCache.put(eventCacheId,event);
    }

    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(Long memberId) {
        return emitterMap.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId+"_"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberLoginId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberLoginId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void deleteById(String id) {
        emitterMap.remove(id);
    }

    public void deleteAllEmitterStartWithId(String memberId) {
        emitterMap.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        emitterMap.remove(key);
                    }
                }
        );
    }

    public void deleteAllEventCacheStartWithId(String memberId) {
        eventCache.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        eventCache.remove(key);
                    }
                }
        );
    }
}
