package com.buck.vsplay.domain.statistics.mapper;

import com.buck.vsplay.domain.statistics.dto.TournamentStatisticsDto;
import com.buck.vsplay.domain.statistics.entity.TournamentStatistics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TournamentStatisticsMapper {


    @Mapping(target = "tournamentName" , source = "topicTournament.tournamentName")
    @Mapping(target = "tournamentStage" , source = "topicTournament.tournamentStage")
    TournamentStatisticsDto.TournamentStatistics toTournamentStatisticsDtoFromEntity(TournamentStatistics tournamentStatistics);

    default List<TournamentStatisticsDto.TournamentStatistics> toTournamentStatisticsDtoList(List<TournamentStatistics> tournamentStatisticsList) {

        return tournamentStatisticsList.stream()
                .map(this::toTournamentStatisticsDtoFromEntity)
                .toList();
    }
}
