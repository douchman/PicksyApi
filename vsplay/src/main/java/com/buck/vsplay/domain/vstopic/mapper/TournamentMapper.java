package com.buck.vsplay.domain.vstopic.mapper;

import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TournamentMapper {

    VsTopicDto.Tournament toTournamentDtoFromEntityWithoutId(TopicTournament topicTournament);
}
