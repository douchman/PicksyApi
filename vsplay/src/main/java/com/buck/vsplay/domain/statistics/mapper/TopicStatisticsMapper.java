package com.buck.vsplay.domain.statistics.mapper;

import com.buck.vsplay.domain.statistics.dto.TopicStatisticsDto;
import com.buck.vsplay.domain.statistics.entity.TopicStatistics;
import com.buck.vsplay.global.util.DateTimeUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface TopicStatisticsMapper {

    @Mapping(target = "firstPlayedAt", qualifiedByName = "formatDateToSting")
    @Mapping(target = "lastPlayedAt", qualifiedByName = "formatDateToSting")
    @Mapping(target = "createdAt", qualifiedByName = "formatDateToSting")
    TopicStatisticsDto.TopicStatistics toTopicStatisticsDtoFromEntity(TopicStatistics topicStatistics);

    @Named("formatDateToSting")
    default String formatDateToSting(LocalDateTime date) {
        return DateTimeUtil.formatDateToSting(date);
    }


}
