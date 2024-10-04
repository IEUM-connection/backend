package com.springboot.utillity;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "i2um.connection@gmail.com";
    private static int number;

    @Autowired
    private static RedisTemplate<String ,String> redisTemplate;

    static ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

    // 5자리 랜덤수
    public static void createNumber() {
        number = (int)(Math.random() * (89999)) + 10000;

        valueOperations.set("certification", Integer.toString(number));
    }

    public int sendMail(String mail) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        if(message != null){
            try {
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, CharEncoding.UTF_8);
                messageHelper.setTo(mail);
                messageHelper.setFrom(senderEmail);
                messageHelper.setSubject("IEUM 인증번호 안내");
                String emailBody = "<div style='font-family: Arial, sans-serif; background-color: #EBFBFF; padding: 60px; text-align: center;'>"
                        + "<div style='background-color: #ffffff; border-radius: 3px; padding: 60px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); max-width: 800px; margin: auto;'>"
                        + "<img src='https://i.imgur.com/5QLLsBJ.png' alt='Logo' style='width: 100px; height: auto; margin-bottom: 30px;' />"
                        + "<h1 style='color: #2c2f33; margin-bottom: 40px;'>이메일 인증</h1>"
                        + "<p style='color: #555; font-size: 13px;'>인증번호입니다</p>"
                        + "<h2 style='color: #7289da; font-size: 24px;'>" + number + "</h2>"
                        + "<p style='color: #555; font-size: 13px;'>인증 번호를 이용해 이메일 인증을 완료해주세요.</p>"
                        + "</div></div>";

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

        return number;
    }

    public boolean checkNumber(String number){
        String value = valueOperations.get("certification");
        return number.equals(value);
    }
}