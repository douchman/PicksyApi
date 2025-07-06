package com.buck.vsplay.domain.vstopic.dto;


import com.buck.vsplay.global.constants.PlayStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryMatchDto {

    @Data
    public static class EntryMatch{
        EntryDto.Entry entryA;
        EntryDto.Entry entryB;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EntryMatchResponse{
        Long matchId;
        String currentTournament;
        PlayStatus playStatus;
        EntryMatch entryMatch;
    }

    @Data
    public static class EntryMatchResultRequest{
        @NotNull(message = "승리 엔트리 비었습니다.")
        Long winnerEntryId;

        @NotNull(message = "패배 엔트리가 비었습니다.")
        Long loserEntryId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateEntryMatchResultResponse{
        String message;
        boolean isAllMatchedCompleted;
        Integer nextTournament;
    }
}
