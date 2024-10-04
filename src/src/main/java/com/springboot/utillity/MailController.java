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

    @PostMapping("/mailSend")
    public HashMap<String, Object> mailSend(@RequestBody Map<String, String> mail) {
        System.out.println(mail.get("mail"));
        HashMap<String, Object> map = new HashMap<>();

        try {
            number = mailService.sendMail(mail.get("mail"));
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return map;
    }

    @GetMapping("/mailCheck")
    public ResponseEntity<?> mailCheck(@RequestParam String userNumber) {

        return ResponseEntity.ok(mailService.checkNumber(userNumber));
    }
}