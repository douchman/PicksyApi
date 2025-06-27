package com.buck.vsplay.domain.vstopic.mapper;


import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TopicEntryMapper {

    TopicEntry toEntityFromCreatedEntryDto(EntryDto.CreateEntry topicEntry);

    @Mapping(target = "mediaUrl", expression = "java(s3Util.getUploadedObjectUrl(topicEntry.getMediaUrl()))")
    @Mapping(target = "thumbnail", expression = "java(s3Util.getUploadedObjectUrl(topicEntry.getThumbnail()))")
    EntryDto.Entry toEntryDtoFromEntity(TopicEntry topicEntry, S3Util s3Util);

    @Mapping(target = "thumbnail", expression = "java(s3Util.getUploadedObjectUrl(topicEntry.getThumbnail()))")
    EntryDto.Entry toEntryDtoFromEntityWithoutSignedMediaUrl(TopicEntry topicEntry, S3Util s3Util);

}
