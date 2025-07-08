package com.buck.vsplay.domain.vstopic.mapper;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VsTopicMapper {

    VsTopic toEntityFromVstopicCreateRequestDtoWithoutThumbnail(VsTopicDto.VsTopicCreateRequest vsTopicCreateRequest);

    @Mapping(target = "thumbnail", expression = "java(s3Util.getUploadedObjectUrl(vsTopic.getThumbnail()))")
    VsTopicDto.VsTopic toVsTopicDtoFromEntityWithPreSignedUrl(VsTopic vsTopic, S3Util s3Util);

    @Mapping(target = "thumbnail", expression = "java(s3Util.getUploadedObjectUrl(vsTopic.getThumbnail()))")
    VsTopicDto.VsTopicWithAccessCode toVsTopicDtoFromEntityWithAccessCode(VsTopic vsTopic, S3Util s3Util);

    @Mapping(target = "thumbnail", expression = "java(s3Util.getUploadedObjectUrl(vsTopic.getThumbnail()))")
    VsTopicDto.VsTopicWithModeration toVsTopicDtoFromEntityWithModeration(VsTopic vsTopic, S3Util s3Util);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateVsTopicFromUpdateRequest(VsTopicDto.VsTopicUpdateRequest vsTopicUpdateRequest, @MappingTarget VsTopic vsTopic);

    default List<VsTopicDto.VsTopic> toVsTopicDtoWithThumbnailListFromEntityList(List<VsTopic> vsTopics, S3Util s3Util) {
        return vsTopics.stream()
                .map(v -> toVsTopicDtoFromEntityWithPreSignedUrl(v, s3Util))
                .toList();
    }

    default List<VsTopicDto.VsTopicWithModeration> toVsTopicDtoWithModerationListFromEntityList(List<VsTopic> vsTopics, S3Util s3Util) {
        return vsTopics.stream()
                .map(v -> toVsTopicDtoFromEntityWithModeration(v, s3Util))
                .toList();
    }
}
