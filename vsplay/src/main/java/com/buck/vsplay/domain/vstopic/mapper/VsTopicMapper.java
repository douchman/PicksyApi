package com.buck.vsplay.domain.vstopic.mapper;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = S3Util.class)
public interface VsTopicMapper {

    @Mapping(target = "thumbnail", ignore = true)
    VsTopic toEntityFromVstopicCreateRequestDtoWithoutThumbnail(VsTopicDto.VsTopicCreateRequest vsTopicCreateRequest);

    VsTopicDto.VsTopic toVsTopicDtoFromEntity(VsTopic vsTopic);

    @Mapping(target = "thumbnail", qualifiedByName = "signedMediaUrl")
    VsTopicDto.VsTopicWithThumbnail toVsTopicDtoFromEntityWithThumbnail(VsTopic vsTopic);

    @Mapping(target = "thumbnail", qualifiedByName = "signedMediaUrl")
    VsTopicDto.VsTopicWithModeration toVsTopicDtoFromEntityWithModeration(VsTopic vsTopic);

    @Mapping(target = "thumbnail", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVsTopicFromUpdateRequest(VsTopicDto.VsTopicUpdateRequest vsTopicUpdateRequest, @MappingTarget VsTopic vsTopic);

    default List<VsTopicDto.VsTopicWithThumbnail> toVsTopicDtoWithThumbnailListFromEntityList(List<VsTopic> vsTopics) {
        return vsTopics.stream()
                .map(this::toVsTopicDtoFromEntityWithThumbnail)
                .toList();
    }

    default List<VsTopicDto.VsTopicWithModeration> toVsTopicDtoWithModerationListFromEntityList(List<VsTopic> vsTopics) {
        return vsTopics.stream()
                .map(this::toVsTopicDtoFromEntityWithModeration)
                .toList();
    }
}
