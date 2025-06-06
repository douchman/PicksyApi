package com.buck.vsplay.domain.gpt.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GptApiDto {

    @Getter
    @Setter
    public static class BadWordApiRequest{
        private List<String> textList;
    }

    @Builder
    @Getter
    @ToString
    public static class GptFilterResult{
        int promptTokens;
        int completionTokens;
        int totalTokens;
        long responseTimeMillis;
    }
}
