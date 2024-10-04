package com.springboot.utillity;

import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Duration;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    @Value("${registration.spring.mail.username}")
    private String senderEmail;
    private int number;
    private long authCodeExpirationMillis = 300000;

    // 빈으로 등록된 redisTemplate에서 불러옴
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;;
    }

    // 5자리 랜덤수
    public void createNumber() {
        // 5자리 난수
        number = (int)(Math.random() * (89999)) + 10000;

        // Redis에 인증번호 등록+300초간 유지
        valueOperations.set("authCode", Integer.toString(number), Duration.ofMillis(authCodeExpirationMillis));
    }

    public int sendMail(String email) {
        valueOperations.set("email", email);
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        if(message != null){
            try {
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, CharEncoding.UTF_8);
                messageHelper.setTo(email);
                messageHelper.setFrom(senderEmail);
                messageHelper.setSubject("IEUM 인증번호 안내");
                String emailBody =
                        // "<div style='font-family: Arial, sans-serif; background-color: #FCBAAA; padding: 60px; text-align: center;'>" +
                        "<div style='background-color: #ffffff; border-radius: 3px; padding: 60px; max-width: 800px; margin: auto; text-align: center;'>"
                        + "<img src='https://i.imgur.com/5QLLsBJ.png' alt='Logo' style='width: 300px; height: auto; margin-bottom: 30px;' />"
                        + "<h1 style='color: #2c2f33; margin-bottom: 40px;'>이메일 인증</h1>"
                        + "<p style='color: #555; font-size: 13px;'>인증번호입니다</p>"
                        + "<h2 style='color: #3BAA4D; font-size: 24px;'>" + number + "</h2>"
                        + "<p style='color: #555; font-size: 13px;'>인증 번호를 이용해 이메일 인증을 완료해주세요.</p>"
                        + "</div>"
                        //+"</div>"
                        ;

                messageHelper.setText(emailBody, true);
                message.saveChanges();

                message.saveChanges();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("message is null");
        }

        javaMailSender.send(message);

        System.out.println(valueOperations.get("certification"));

        return number;
    }

    public boolean checkNumber(String email, String authCode){
        return authCode.equals(valueOperations.get("authCode")) && email.equals(valueOperations.get("email"));
    }
}