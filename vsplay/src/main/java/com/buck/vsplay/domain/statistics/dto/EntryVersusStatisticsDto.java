package com.buck.vsplay.domain.statistics.dto;


import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryVersusStatisticsDto {


    @Setter
    @Getter
    @Builder
    public static class OpponentEntryInfoWithMatchRecord{
        EntryDto.Entry opponentEntry;
        MatchRecord matchRecord;
    }

    @Getter
    @Setter
    @Builder
    public static class MatchRecord{
        Integer totalMatches;
        Integer wins;
        Integer losses;
        Double winRate;
    }

    @Getter
    @Setter
    @Builder
    public static class EntryVersusStatisticsResponse{
        List<OpponentEntryInfoWithMatchRecord> matchUpRecords;
    }
}
