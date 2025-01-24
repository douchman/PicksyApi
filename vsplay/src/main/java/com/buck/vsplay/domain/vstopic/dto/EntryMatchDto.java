package com.buck.vsplay.domain.vstopic.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryMatchDto {

    @Data
    public static class EntryMatch{
        EntryDto.Entry entryA;
        EntryDto.Entry entryB;
    }

    @Data
    public static class EntryMatchResponse{
        Long matchId;
        EntryMatch entryMatch = new EntryMatch();
    }

    @Data
    public static class EntryMatchResultRequest{
        @NotNull(message = "승리 엔트리 비었습니다.")
        Long winnerEntryId;

        @NotNull(message = "패배 엔트리가 비었습니다.")
        Long loserEntryId;
    }
}
