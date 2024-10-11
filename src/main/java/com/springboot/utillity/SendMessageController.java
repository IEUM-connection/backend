package com.springboot.utillity;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendMessageController {
    private final SendMessageService sendMessageService;

    public SendMessageController(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @PostMapping("/send-sms")
    public ResponseEntity<String> sendMessage(@RequestBody SmsDto requestDto) {

        sendMessageService.sendMessage(requestDto.getBody(), requestDto.getTo(), requestDto.getFrom());

        return ResponseEntity.ok(requestDto.getBody());
    }
}
