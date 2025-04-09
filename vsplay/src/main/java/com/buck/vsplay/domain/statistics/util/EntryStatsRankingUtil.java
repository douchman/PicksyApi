package com.buck.vsplay.domain.statistics.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryStatsRankingUtil {

    private static final double WIN_RATE_WEIGHT = 0.5; // 승률 점수 가중치
    private static final double TOTAL_MATCHES_WEIGHT = 0.3; // 총 대곃 횟수 점수 가중치
    private static final double TOTAL_WINS_WEIGHT = 0.2; // 총 승리 횟수 점수 가중치

    public static double calculateRankingScore(Double winRate, Integer totalMatches, Integer totalWins){
        double winRateScore = winRate * WIN_RATE_WEIGHT;
        double totalMatchesScore = totalMatches * TOTAL_MATCHES_WEIGHT;
        double totalWinsScore = totalWins * TOTAL_WINS_WEIGHT;

        return winRateScore + totalMatchesScore + totalWinsScore;
    }

}
