package com.buck.vsplay.domain.inquiry.service.support;

import com.buck.vsplay.domain.inquiry.dto.InquiryDto;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class InquirySlack {
    @Value("${slack.webhook.inquiry}")
    private String webHookUrl; // 문의 채널 webHook


    public void sendInquiryCreatedSlackMessage(InquiryDto.InquiryCreateRequest request) throws IOException {

        String inquiryCreatedMsgTemplate = """
                📩 *[Picksy 문의 도착]*
                *문의 유형:* %s
                *문의자 명:* %s
                *문의자 이메일:* %s
                *문의 제목:* %s
                *문의 내용*
                %s
                """.formatted(
                request.getInquiryType().getDescription(),
                request.getAuthor(),
                request.getEmail(),
                request.getTitle(),
                request.getContent());


        Slack slack = Slack.getInstance();
        Payload payload = Payload.builder()
                .text(inquiryCreatedMsgTemplate)
                .build();

        slack.send(webHookUrl, payload);
    }

}
