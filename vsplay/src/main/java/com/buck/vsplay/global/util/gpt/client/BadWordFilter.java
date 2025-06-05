package com.buck.vsplay.global.util.gpt.client;

import com.buck.vsplay.global.util.gpt.prompt.GptApiPrompt;
import com.buck.vsplay.global.util.gpt.prompt.GptPromptType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BadWordFilter {

    @Value("${app.gpt-api-key}")
    private static String openaiApiKey;

    public void filterTextContent(List<String> textList){
        String gptApiUrl = "https://api.openai.com/v1/chat/completions";
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        try {
            // 프롬프트 설정
            Map<String, Object> systemMsg = Map.of(
                    "role", "system",
                    "content", GptApiPrompt.getSystemPrompt(GptPromptType.BAD_WORD_CHECK)
            );

            Map<String, Object> userMsg = Map.of("role", "user", "content", createBadWordGptApiInput(textList));

            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-4-turbo",
                    "messages", List.of(systemMsg, userMsg)
            );

            // 요청 전송
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(openaiApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // 응답 추출 및 파싱
            long start = System.currentTimeMillis();
            ResponseEntity<String> response = restTemplate.postForEntity(gptApiUrl, request, String.class);
            long elapsed = System.currentTimeMillis() - start;

            JsonNode root = objectMapper.readTree(response.getBody());
            String content = root.path("choices").get(0).path("message").path("content").asText();

            Map<String, Boolean> resultMap = objectMapper.readValue(content,new TypeReference<>() {});

            JsonNode usageNode = root.path("usage");
            int promptTokens = usageNode.path("prompt_tokens").asInt();
            int completionTokens = usageNode.path("completion_tokens").asInt();
            int totalTokens = usageNode.path("total_tokens").asInt();


        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    private String createBadWordGptApiInput(List<String> stringList) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, String>> gptInputMap = new LinkedHashMap<>();

        if( stringList != null && !stringList.isEmpty()){
            for(int i = 0; i < stringList.size(); i++) {
                String key = String.valueOf(i + 1);
                gptInputMap.put(key, Map.of("text", stringList.get(i)));
            }
        }
        return objectMapper.writeValueAsString(gptInputMap);
    }

}
