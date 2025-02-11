package com.buck.vsplay.domain.statistics.dto;


import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryVersusStatisticsDto {

    @Getter
    @Builder
    public static class EntryVersusStatistics{
        EntryDto.Entry opponentEntry;
        Integer totalMatches;
        Integer wins;
        Integer losses;
        Double winRate;
    }

}
