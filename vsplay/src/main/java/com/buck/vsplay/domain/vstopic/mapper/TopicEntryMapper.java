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

    /**
     * 엔티티 업데이트를 위해 DTO 의 값을 기존 Entry 엔티티에 적용
     * Null 필드는 자동으로 제외
     * @param updatedRequest  기존 엔트리에 업데이트 할 변경된 엔트리 값
     * @param topicEntry 업데이트를 적용할 기존의 엔트리
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void applyChangesToTopicEntry(EntryDto.UpdateEntryRequest updatedRequest, @MappingTarget TopicEntry topicEntry);

}
