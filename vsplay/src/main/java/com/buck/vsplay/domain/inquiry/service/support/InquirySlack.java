package com.buck.vsplay.domain.inquiry.service.support;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class InquirySlack {
    private static final String WEB_HOOK_URL = "https://hooks.slack.com/services/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"; // 문의 채널 webHook


    public void sendInquiryCreatedSlackMessage(String author, String email, String title, String content) throws IOException {

        String inquiryCreatedMsgTemplate = """
                📩 *[Picksy 문의 도착]*
                *문의자 명:* %s
                *문의자 이메일:* %s
                *문의 제목:* %s
                *문의 내용*
                %s
                """.formatted(author, email, title, content);


        Slack slack = Slack.getInstance();
        Payload payload = Payload.builder()
                .text(inquiryCreatedMsgTemplate)
                .build();

        slack.send(WEB_HOOK_URL, payload);
    }

}
