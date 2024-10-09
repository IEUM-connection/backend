package com.springboot.utillity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.springboot.member.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class FirebaseCloudMessageService {

    private final MemberService memberService;

    public FirebaseCloudMessageService(MemberService memberService) {
        this.memberService = memberService;
    }

    public String sendMessage(FcmMessageDto requestDto) {

        if(requestDto.getToken() != null && requestDto.getToken().length() > 0){
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(requestDto.getTitle())
                            .setBody(requestDto.getBody())
                            .setImage("https://i.imgur.com/5QLLsBJ.png")
                            .build())
                    .setToken(requestDto.getToken())
                    .build();

            try {
                // Send a message to the devices subscribed to the provided topic.
                String response = FirebaseMessaging.getInstance().send(message);
                // Response is a message ID string.
                return "Successfully sent message: " + response + "\n " + requestDto.getToken() + ", " + requestDto.getTitle() + ", " + requestDto.getBody();
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
                return "Failed to send message";
            }

        }
        else {
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(requestDto.getTitle())
                            .setBody(requestDto.getBody())
                            .setImage("https://i.imgur.com/5QLLsBJ.png")
                            .build())
                    .setTopic(requestDto.getTopic())
                    .build();

            try {
                // Send a message to the devices subscribed to the provided topic.
                String response = FirebaseMessaging.getInstance().send(message);
                // Response is a message ID string.
                return "Successfully sent message: " + response + "\n " + requestDto.getTopic() + ", " + requestDto.getTitle() + ", " + requestDto.getBody();
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
                return "Failed to send message";
            }
        }


    }
}