package com.springboot.utillity;

import com.springboot.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService, MemberService memberService) {
        this.messageService = messageService;
    }

    @PostMapping("/send-sms")
    public ResponseEntity<String> sendMessage(@RequestBody Message requestDto) {
        messageService.sendMessage(requestDto);

        return ResponseEntity.ok(requestDto.getBody());
    }

    @PostMapping("/send-allsms")
    public ResponseEntity<String> sendAllMessage(@RequestBody Message requestDto) {
        messageService.sendAllMessage(requestDto);

        return ResponseEntity.ok(requestDto.getBody());
    }
}
