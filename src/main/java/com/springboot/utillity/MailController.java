package com.springboot.utillity;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MailController {

    private final MailService mailService;
    private int number;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/email-code")
    public HashMap<String, Object> mailSend(@RequestBody Map<String, String> mail) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            number = mailService.sendMail(mail.get("email"));
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return map;
    }

    @GetMapping("/verify-email-code")
    public ResponseEntity<?> mailCheck(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(mailService.checkNumber(body.get("email"), body.get("authCode")));
    }
}