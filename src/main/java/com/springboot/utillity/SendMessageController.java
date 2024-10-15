package com.springboot.utillity;

import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendMessageController {
    private final SendMessageService sendMessageService;

    public SendMessageController(SendMessageService sendMessageService, MemberService memberService) {
        this.sendMessageService = sendMessageService;
    }

    @PostMapping("/send-sms")
    public ResponseEntity<String> sendMessage(@RequestBody SmsDto requestDto, Authentication authentication) {
        String memberCode = authentication.getName();
        sendMessageService.sendMessage(requestDto, memberCode);

        return ResponseEntity.ok(requestDto.getBody());
    }
}
