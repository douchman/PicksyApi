package com.buck.vsplay.domain.statistics.dto;


import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryStatisticsDto {

    @Setter
    @Getter
    public static class EntryStatistics{
        Integer totalMatches;
        Integer totalWins;
        Integer totalLosses;
        Double winRate;
        Integer highestTournament;
    }

    @Getter
    @Setter
    @Builder
    public static class EntryStatWithEntryInfo {
        EntryDto.Entry entry;
        EntryStatistics statistics;
    }



    @Getter
    @Setter
    @AllArgsConstructor
    public static class EntryStatWithEntryInfoList {
        List<EntryStatWithEntryInfo> entriesStatistics;
    }
}
