package com.gaduationproject.cre8.app.notify.controller;

import com.fasterxml.jackson.databind.ser.Serializers.Base;
import com.gaduationproject.cre8.app.auth.interfaces.CurrentMemberLoginId;
import com.gaduationproject.cre8.app.notify.service.NotifyService;
import com.gaduationproject.cre8.app.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/notify")
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@CurrentMemberLoginId final String loginId,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") final String lastEventId) {

        return notifyService.subscribe(loginId, lastEventId);
    }



}
