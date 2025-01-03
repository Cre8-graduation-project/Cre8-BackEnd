package com.cre8.chat.event;

import lombok.Getter;
import org.springframework.messaging.Message;

@Getter
public class SessionSubscribedEvent {

     private final Message message;



     public SessionSubscribedEvent(Message message){
          this.message = message;

     }

}
