package com.buck.vsplay.global.util.gpt.prompt;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GptApiPrompt {

    public static final Map<GptPromptType, String> promptMap = Map.of(
            GptPromptType.BAD_WORD_CHECK, """
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

    public static String getSystemPrompt(GptPromptType type) {
        return promptMap.get(type);
    }
}
