package com.buck.vsplay.domain.vstopic.mapper;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VsTopicMapper {

    VsTopic toEntityFromVstopicCreateRequestDtoWithoutThumbnail(VsTopicDto.VsTopicCreateRequest vsTopicCreateRequest);

    VsTopicDto.VsTopic toVsTopicDtoFromEntity(VsTopic vsTopic);

    @Mapping(target = "thumbnail", expression = "java(s3Util.getUploadedObjectUrl(vsTopic.getThumbnail()))")
    VsTopicDto.VsTopicWithThumbnail toVsTopicDtoFromEntityWithThumbnail(VsTopic vsTopic, S3Util s3Util);

    @Mapping(target = "thumbnail", expression = "java(s3Util.getUploadedObjectUrl(vsTopic.getThumbnail()))")
    VsTopicDto.VsTopicWithModeration toVsTopicDtoFromEntityWithModeration(VsTopic vsTopic, S3Util s3Util);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVsTopicFromUpdateRequest(VsTopicDto.VsTopicUpdateRequest vsTopicUpdateRequest, @MappingTarget VsTopic vsTopic);

    default List<VsTopicDto.VsTopicWithThumbnail> toVsTopicDtoWithThumbnailListFromEntityList(List<VsTopic> vsTopics, S3Util s3Util) {
        return vsTopics.stream()
                .map(v -> toVsTopicDtoFromEntityWithThumbnail(v, s3Util))
                .toList();
    }

    default List<VsTopicDto.VsTopicWithModeration> toVsTopicDtoWithModerationListFromEntityList(List<VsTopic> vsTopics, S3Util s3Util) {
        return vsTopics.stream()
                .map(v -> toVsTopicDtoFromEntityWithModeration(v, s3Util))
                .toList();
    }
}
