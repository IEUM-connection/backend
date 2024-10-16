package com.springboot.utillity;

import com.springboot.member.entity.Guardian;
import com.springboot.member.entity.Member;
import com.springboot.member.service.AdminService;
import com.springboot.member.service.GuardianService;
import com.springboot.member.service.MemberService;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    private DefaultMessageService messageService;
    private final MemberService memberService;
    private final GuardianService guardianService;

    @Value("${nurigo.apikey}")
    private String apikey;
    @Value("${nurigo.apisecret}")
    private String apisecret;
    @Value("${nurigo.phonenum}")
    private String phonenum;

    public MessageService(AdminService adminService, MemberService memberService, GuardianService guardianService) {
        this.memberService = memberService;
        this.guardianService = guardianService;
    }

    public void sendMessage(Message msg) {
        net.nurigo.sdk.message.model.Message message = new net.nurigo.sdk.message.model.Message();

        if (messageService == null) {
            this.messageService = NurigoApp.INSTANCE.initialize(apikey, apisecret, "https://api.coolsms.co.kr");
        }

        ArrayList<net.nurigo.sdk.message.model.Message> messageList = new ArrayList<>();

        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom(phonenum);
        message.setTo(msg.getGudianNum());
        message.setText(msg.getBody());

        messageList.add(message);

        if (msg.getAdminNum() != null && msg.getAdminNum() != "") {
            message = new net.nurigo.sdk.message.model.Message();

            message.setFrom(phonenum);
            message.setTo(msg.getAdminNum());
            message.setText(msg.getBody());

            messageList.add(message);
        }

        try {
            // send 메소드로 단일 Message 객체를 넣어도 동작합니다!
            // 세 번째 파라미터인 showMessageList 값을 true로 설정할 경우 MultipleDetailMessageSentResponse에서 MessageList를 리턴하게 됩니다!
            MultipleDetailMessageSentResponse response = this.messageService.send(messageList, false, true);

            // 중복 수신번호를 허용하고 싶으실 경우 위 코드 대신 아래코드로 대체해 사용해보세요!
            //MultipleDetailMessageSentResponse response = this.messageService.send(messageList, true);

            System.out.println(response);
        } catch (NurigoMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void sendAllMessage(Message msg) {
        net.nurigo.sdk.message.model.Message message = new net.nurigo.sdk.message.model.Message();

        if (messageService == null) {
            this.messageService = NurigoApp.INSTANCE.initialize(apikey, apisecret, "https://api.coolsms.co.kr");
        }

        ArrayList<net.nurigo.sdk.message.model.Message> messageList = new ArrayList<>();

        // 멤버에게
        if(msg.getIsMember()){
            List<Member> members = memberService.getMembers(Member.MemberStatus.ACTIVE);
            for (int i = 0; i < members.size(); i++){
                message.setFrom(phonenum);
                message.setTo(members.get(i).getPhone().replaceAll("-", ""));
                message.setText(msg.getBody());

                messageList.add(message);
            }
        }
        else {
            // 가디언에게
            List<Guardian> guardians = guardianService.getGuardians(Guardian.GuardianStatus.GUARDIAN_ACTIVE);
            for (int i = 0; i < guardians.size(); i++){
                message.setFrom(phonenum);
                message.setTo(guardians.get(i).getPhone().replaceAll("-", ""));
                message.setText(msg.getBody());

                messageList.add(message);
            }
        }

        try {
            // send 메소드로 단일 Message 객체를 넣어도 동작합니다!
            // 세 번째 파라미터인 showMessageList 값을 true로 설정할 경우 MultipleDetailMessageSentResponse에서 MessageList를 리턴하게 됩니다!
            MultipleDetailMessageSentResponse response = this.messageService.send(messageList, false, true);

            System.out.println(response);
        } catch (NurigoMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

}