package com.buck.vsplay.global.util.gpt.client;

import com.buck.vsplay.global.util.gpt.entity.GptUsageLog;
import com.buck.vsplay.global.util.gpt.exception.GptException;
import com.buck.vsplay.global.util.gpt.exception.GptExceptionCode;
import com.buck.vsplay.global.util.gpt.model.GptModelType;
import com.buck.vsplay.global.util.gpt.prompt.GptApiPrompt;
import com.buck.vsplay.global.util.gpt.prompt.GptPromptType;
import com.buck.vsplay.global.util.gpt.repository.GptUsageLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BadWordFilter {

    private final GptUsageLogRepository gptUsageLogRepository;

    @Value("${app.gpt-api-key}")
    private static String openaiApiKey;

    public void filterTextContent(List<String> textList){
        boolean apiSuccess = true;
        GptModelType gptMdodel = GptModelType.GPT_4_1;
        String gptApiUrl = "https://api.openai.com/v1/chat/completions";
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        int promptTokens = 0;
        int completionTokens = 0;
        int totalTokens = 0;

        long start = System.currentTimeMillis();
        long responseTime = 0L;
        String apiInput = "";
        String errorCode = "";

        try {
            apiInput =createBadWordGptApiInput(textList); // 입력 변환

            // 프롬프트 설정
            Map<String, Object> systemMsg = Map.of(
                    "role", "system",
                    "content", GptApiPrompt.getSystemPrompt(GptPromptType.BAD_WORD_CHECK)
            );

            Map<String, Object> userMsg = Map.of("role", "user", "content", apiInput);

            Map<String, Object> requestBody = Map.of(
                    "model", gptMdodel.getModelName(),
                    "messages", List.of(systemMsg, userMsg)
            );

            // API 호출
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(openaiApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // 응답 추출 및 파싱
            ResponseEntity<String> response = restTemplate.postForEntity(gptApiUrl, request, String.class);
            responseTime = System.currentTimeMillis() - start;

            JsonNode root = objectMapper.readTree(response.getBody());
            String content = root.path("choices").get(0).path("message").path("content").asText();

            // 결과 파싱
            Map<String, Boolean> resultMap = objectMapper.readValue(content,new TypeReference<>() {});

            // 소모 토큰
            JsonNode usageNode = root.path("usage");
            promptTokens = usageNode.path("prompt_tokens").asInt();
            completionTokens = usageNode.path("completion_tokens").asInt();
            totalTokens = usageNode.path("total_tokens").asInt();

        } catch (JsonProcessingException e) {
            apiSuccess = false;
            errorCode = GptExceptionCode.GPT_API_ERROR.getErrorCode();
            throw new GptException(GptExceptionCode.GPT_API_ERROR);
        } catch (RestClientException e){
            apiSuccess = false;
            errorCode = GptExceptionCode.GPT_COMMUNICATION_ERROR.getErrorCode();
            throw new GptException(GptExceptionCode.GPT_COMMUNICATION_ERROR);
        }
        finally {
            // 로그 저장
            GptUsageLog usageLog = GptUsageLog.builder()
                    .promptType(GptPromptType.BAD_WORD_CHECK)
                    .model(gptMdodel.getModelName())
                    .promptTokens(promptTokens)
                    .completionTokens(completionTokens)
                    .totalTokens(totalTokens)
                    .responseTimeMills(responseTime)
                    .estimatedCost(gptMdodel.getRate().calculateCost(promptTokens, completionTokens))
                    .inputPreview(apiInput)
                    .success(apiSuccess)
                    .errorCode(errorCode)
                    .build();

            gptUsageLogRepository.save(usageLog);
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
