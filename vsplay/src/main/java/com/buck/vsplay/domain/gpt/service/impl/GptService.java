package com.buck.vsplay.domain.gpt.service.impl;

import com.buck.vsplay.domain.gpt.dto.GptApiDto;
import com.buck.vsplay.domain.gpt.exception.GptApiException;
import com.buck.vsplay.domain.gpt.exception.GptApiExceptionCode;
import com.buck.vsplay.domain.gpt.service.IGptService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GptService implements IGptService {

    @Value("${app.gpt-api-key}")
    private String openaiApiKey;

    @Override
    public void moderateTextContent(GptApiDto.BadWordApiRequest badWordApiRequest) {
        String apiUrl = "https://api.openai.com/v1/chat/completions";
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        try {
            // 프롬프트 설정
            Map<String, Object> systemMsg = Map.of(
                    "role", "system",
                    "content", """
                            너는 한국어 문장을 검토하여 비속어(욕설, 혐오, 불쾌한 표현 등)가 포함되어 있는지 판단하는 역할을 수행해.
                            사용자로부터 다음과 같은 JSON 형태의 입력을 받는다:
                    
                            {
                              "1": { "text": "문장1" },
                              "2": { "text": "문장2" }
                            }
                    
                            각 키("1", "2", ...)는 고유 ID이므로 절대로 변경하지 말고,
                            응답은 다음과 같이 key를 그대로 유지한 채로 true/false 값을 반환해줘:
                    
                            {
                              "1": true,
                              "2": false
                            }
                    
                            참고:
                            - '나쁜 녀석들', '꺼져줄래요?' 등 애매하거나 문맥상 욕설이 아닌 표현은 false로 간주해.
                            - 판단 기준은 맥락에 기반한 명백한 비속어/욕설 여부야.
                            - 반드시 위와 같이 JSON 형태로만 응답해.
                        """
            );



            Map<String, Object> userMsg = Map.of("role", "user", "content", createBadWordGptApiInput(badWordApiRequest.getTextList()));

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
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            long elapsed = System.currentTimeMillis() - start;

            JsonNode root = objectMapper.readTree(response.getBody());
            String content = root.path("choices").get(0).path("message").path("content").asText();

            Map<String, Boolean> resultMap = objectMapper.readValue(content,new TypeReference<>() {});

            JsonNode usageNode = root.path("usage");
            int promptTokens = usageNode.path("prompt_tokens").asInt();
            int completionTokens = usageNode.path("completion_tokens").asInt();
            int totalTokens = usageNode.path("total_tokens").asInt();

            GptApiDto.GptFilterResult gptFilterResult = GptApiDto.GptFilterResult.builder()
                    .promptTokens(promptTokens)
                    .completionTokens(completionTokens)
                    .totalTokens(totalTokens)
                    .responseTimeMillis(elapsed)
                    .build();

            log.info("resultMap -> {}", resultMap);
            log.info("GptFilterResult -> {}", gptFilterResult.toString());

        } catch (JsonProcessingException e) {
            log.error("GPT 비속어 필터링 실패", e);
            throw new GptApiException(GptApiExceptionCode.GPT_API_ERROR);
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
