package com.buck.vsplay.global.util.gpt.model;

public class GptModelRate {
    private final double promptRatePer1K;     // USD per 1K tokens / 천 토큰당
    private final double completionRatePer1K; // USD per 1K tokens / 천 토큰당

    public GptModelRate(double promptRatePer1K, double completionRatePer1K) {
        this.promptRatePer1K = promptRatePer1K;
        this.completionRatePer1K = completionRatePer1K;
    }

    public double calculateCost(int promptTokens, int completionTokens) {
        return (promptTokens * promptRatePer1K + completionTokens * completionRatePer1K) / 1000.0;
    }
}
