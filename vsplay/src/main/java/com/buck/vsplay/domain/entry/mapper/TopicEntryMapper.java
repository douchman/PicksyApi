package com.buck.vsplay.domain.entry.mapper;


import com.buck.vsplay.domain.entry.dto.EntryDto;
import com.buck.vsplay.domain.entry.entity.TopicEntry;
import com.buck.vsplay.global.constants.MediaType;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TopicEntryMapper {

    TopicEntry toEntityFromCreatedEntryDto(EntryDto.CreateEntry topicEntry);

    @Mapping(target = "mediaUrl", expression = "java(resolveMediaUrl(topicEntry, s3Util))")
    @Mapping(target = "thumbnail", expression = "java(s3Util.getUploadedObjectUrl(topicEntry.getThumbnail()))")
    EntryDto.Entry toEntryDtoFromEntryEntity(TopicEntry topicEntry, S3Util s3Util);

    default String resolveMediaUrl(TopicEntry topicEntry, S3Util s3Util) {
        if(topicEntry.getMediaType() == MediaType.YOUTUBE){
            return topicEntry.getMediaUrl();
        }

        return s3Util.getUploadedObjectUrl(topicEntry.getMediaUrl());
    }
}
