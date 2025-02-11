package com.buck.vsplay.domain.statistics.mapper;

import com.buck.vsplay.domain.statistics.dto.EntryStatisticsDto;
import com.buck.vsplay.domain.statistics.entity.EntryStatistics;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntryStatisticsMapper {

    EntryStatisticsDto.EntryStatistics toEntryStatisticsDtoFromEntity(EntryStatistics entryStatistics);
}
