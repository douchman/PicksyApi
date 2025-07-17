package com.buck.vsplay.domain.contact.service.support;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InquiryEmail {

    private final JavaMailSender mailSender;

    public void sendInquiryMail(String userName, String userEmail, String title, String content){
        // TODO : SES SandBox 해제 시 Mimemessage 로 전환 필요
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("no-reply@buck93.com");
        message.setTo("take6948@gmail.com");
        message.setReplyTo(userEmail);
        message.setSubject("\uD83D\uDCE2  [Picksy 문의] " + title);
        message.setText("""
                ✅Picksy 문의가 도착했습니다.
                - 문의자 명 : %s
                - 문의자 이메일 : %s
                - 문의 내용 :
                %s
                """.formatted(userName, userEmail, content));
        mailSender.send(message);
    }
}
