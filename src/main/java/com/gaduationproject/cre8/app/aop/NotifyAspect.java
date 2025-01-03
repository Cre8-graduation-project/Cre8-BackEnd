package com.gaduationproject.cre8.common.aop;

import com.gaduationproject.cre8.app.notify.service.NotifyService;
import com.gaduationproject.cre8.externalApi.mongodb.domain.Notify;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class NotifyAspect {

    private final NotifyService notifyService;

    @Pointcut("@annotation(com.gaduationproject.cre8.app.notify.annotation.SendNotify)")
    public void annotationPointcut(){

    }

    @AfterReturning(pointcut = "annotationPointcut()",returning = "result")
    public void sendNotify(JoinPoint joinPoint, NotifyInfo result){

        notifyService.saveNotify(Notify.builder()
                .contents(result.getSenderNickName()+"이 "+ result.getContents()+"라고 하네요~")
                .postId(result.getPostId())
                .read(false)
                .memberId(result.receiver().getId())
                .notificationType(result.getNotificationType())
                .build());

    }


}
