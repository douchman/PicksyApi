package com.buck.vsplay.domain.vstopic.mapper;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VsTopicMapper {
    @Mapping(target = "thumbnail", ignore = true)
    VsTopic toEntity(VsTopicDto.VsTopicCreateRequest vsTopicCreateRequest);

    @Mapping(target = "thumbnail", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVsTopicFromUpdateRequest(VsTopicDto.VsTopicUpdateRequest vsTopicUpdateRequest, @MappingTarget VsTopic vsTopic);

    VsTopicDto.VsTopic toVsTopicDto(VsTopic vsTopic);
    VsTopicDto.Tournament toTournamentDtoFromTournamentEntity(TopicTournament topicTournament);
    List<VsTopicDto.Tournament> toTournamentDtoListFromTournamentEntityList(List<TopicTournament> topicTournaments);

    @Mapping(target = "topic", expression = "java(toVsTopicDto(vsTopic))")
    @Mapping(target = "tournamentList", expression = "java(toTournamentDtoListFromTournamentEntityList(tournamentList))")
    VsTopicDto.VsTopicDetailWithTournamentsResponse toVsTopicDetailWithTournaments(
            VsTopic vsTopic, List<TopicTournament> tournamentList
    );

}
