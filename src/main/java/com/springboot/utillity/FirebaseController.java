package com.springboot.utillity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirebaseController {

    private final FirebaseCloudMessageService firebaseMessageService;

    @Autowired
    public FirebaseController(FirebaseCloudMessageService firebaseMessageService) {
        this.firebaseMessageService = firebaseMessageService;
    }

    @PostMapping("/fcm-sendMessage")
    public ResponseEntity<String> sendMessage(@RequestBody FcmMessageDto requestDto) {
        String response = firebaseMessageService.sendMessage(requestDto);
        return ResponseEntity.ok(response);
    }
}