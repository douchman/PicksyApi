package com.buck.vsplay.domain.vstopic.mapper;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = S3Util.class)
public interface VsTopicMapper {

    @Mapping(target = "thumbnail", ignore = true)
    VsTopic toEntityFromVstopicCreateRequestDtoWithoutThumbnail(VsTopicDto.VsTopicCreateRequest vsTopicCreateRequest);

    VsTopicDto.VsTopic toVsTopicDtoFromEntity(VsTopic vsTopic);

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

    @Named("signedMediaUrl")
    default String signedMediaUrl(String mediaUrl, @Context S3Util s3Util) {
        return s3Util.getUploadedObjectUrl(mediaUrl);
    }

    default List<VsTopicDto.VsTopic> toVsTopicDtoListFromEntityList(List<VsTopic> vsTopics) {
        return vsTopics.stream()
                .map(this::toVsTopicDtoFromEntity)
                .toList();
    }
}
