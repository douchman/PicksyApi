package com.buck.vsplay.domain.gpt.service;

import com.buck.vsplay.domain.gpt.dto.GptApiDto;

public interface IGptService {
    void moderateTextContent(GptApiDto.BadWordApiRequest badWordApiRequest);
}
