package com.buck.vsplay.domain.vstopic.mapper;


import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicEntryMapper {
    TopicEntryMapper INSTANCE = Mappers.getMapper(TopicEntryMapper.class);

    @Mapping(target = "topic", expression = "java(vsTopic)")
    TopicEntry toTopicEntryWithTopic(EntryDto.Entry topicEntry, @Context VsTopic vsTopic);

    @Mapping(source = "id", target ="entryId")
    @Mapping(target = "mediaUrl" , expression = "java(s3Util.getUploadedObjectUrl(topicEntry.getMediaUrl()))")
    EntryDto.CreatedEntry toCreatedEntry(TopicEntry topicEntry, @Context S3Util s3Util);

    default List<EntryDto.CreatedEntry> toCreatedEntryDtoList(List<TopicEntry> entries, @Context S3Util s3Util){
        return entries.stream()
                .map(entry -> toCreatedEntry(entry, s3Util))
                .toList();
    }
}
