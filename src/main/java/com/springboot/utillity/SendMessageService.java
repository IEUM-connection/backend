package com.springboot.utillity;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SendMessageService {
    private static final Logger logger = LoggerFactory.getLogger(SendMessageService.class);

    private DefaultMessageService messageService;

    @Value("${nurigo.apikey}")
    private String apikey;
    @Value("${nurigo.apisecret}")
    private String apisecret;

    public void sendMessage(String body, String to, String from){
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom(from);
        message.setTo(to);
        message.setText(body);

        if(messageService == null) {
            this.messageService = NurigoApp.INSTANCE.initialize(apikey, apisecret, "https://api.coolsms.co.kr");
        }

        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);
    }
}