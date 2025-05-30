package com.buck.vsplay.domain.statistics.dto;


import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.global.constants.OrderType;
import com.buck.vsplay.global.dto.Pagination;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryStatisticsDto {

    @Setter
    @Getter
    public static class EntryStatistics{
        Integer entryRank;
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
    public static class EntryStatSearchRequest{
        private String keyword;
        private OrderType rankOrderType = OrderType.ASC;
        private OrderType totalMatchesOrderType = OrderType.NONE;
        private OrderType totalWinsOrderType = OrderType.NONE;
        private OrderType winRateOrderType = OrderType.NONE;
        private Integer page = 1;
        private Integer pageSize = 20;
    }


    @Getter
    @Setter
    @Builder
    public static class EntryStatSearchResponse {
        List<EntryStatWithEntryInfo> entriesStatistics;
        String lastUpdatedAt;
        Pagination pagination;
    }

    @Getter
    @Setter
    @Builder
    public static class SingleEntryStatsResponse{
        EntryDto.Entry entry;
        EntryStatistics statistics;
    }
}
