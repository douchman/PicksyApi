package com.buck.vsplay.domain.vstopic.mapper;


import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TopicEntryMapper {
    TopicEntryMapper INSTANCE = Mappers.getMapper(TopicEntryMapper.class);

    @Mapping(target = "topic", expression = "java(vsTopic)")
    TopicEntry toTopicEntryWithTopic(EntryDto.Entry topicEntry, @Context VsTopic vsTopic);
}
