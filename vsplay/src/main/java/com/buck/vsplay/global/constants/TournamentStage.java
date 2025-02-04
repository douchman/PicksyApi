package com.buck.vsplay.global.constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum TournamentStage {

    STAGE2(2, "결승전"),
    STAGE4(4, "준결승전"),
    STAGE8(8, "8강"),
    STAGE16(16, "16강"),
    STAGE32(32, "32강"),
    STAGE64(64, "64강"),
    STAGE128(128, "128강")
    ;

    private final int stage;
    private final String stageName;

    TournamentStage(int stage, String stageName){
        this.stage = stage;
        this.stageName = stageName;
    }

    public static Optional<String> findStageNameByStage(int stage) {
        return Arrays.stream(values())
                .filter(tournamentStage -> tournamentStage.stage == stage)
                .map(TournamentStage::getStageName)
                .findFirst();
    }
}
