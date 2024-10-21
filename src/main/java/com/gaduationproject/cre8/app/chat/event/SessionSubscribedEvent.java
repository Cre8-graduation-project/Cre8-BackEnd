package com.gaduationproject.cre8.app.chat.event;

import com.gaduationproject.cre8.app.chat.handler.StompPreHandler;
import lombok.Builder;
import lombok.Getter;
import org.springframework.messaging.Message;

@Getter
public class SessionSubscribedEvent {

     private final Message message;



     public SessionSubscribedEvent(Message message){
          this.message = message;

     }

}
