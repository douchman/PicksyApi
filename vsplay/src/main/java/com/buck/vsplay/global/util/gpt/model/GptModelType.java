package com.buck.vsplay.global.util.gpt.model;

import lombok.Getter;

@Getter
public enum GptModelType {
    GPT_4_TURBO("gpt-4-turbo", new GptModelRate(0.01, 0.03)),
    GPT_4_1("gpt-4.1", new GptModelRate(0.002, 0.008));

    private final String modelName;
    private final GptModelRate rate;

    GptModelType(String modelName, GptModelRate rate) {
        this.modelName = modelName;
        this.rate = rate;
    }
}