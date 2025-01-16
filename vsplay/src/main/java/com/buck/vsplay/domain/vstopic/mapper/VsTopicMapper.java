package com.buck.vsplay.domain.vstopic.mapper;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VsTopicMapper {
    VsTopicMapper INSTANCE = Mappers.getMapper(VsTopicMapper.class);
    VsTopic toEntity(VsTopicDto.VsTopic vsTopic);
    @Mapping(target = "thumbnail", ignore = true)
    VsTopic toEntity(VsTopicDto.VsTopicCreateRequest vsTopicCreateRequest);
    VsTopicDto.VsTopic toDto(VsTopic vsTopic);

    @Mapping(target = "thumbnail", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVsTopicFromUpdateRequest(VsTopicDto.VsTopicUpdateRequest vsTopicUpdateRequest, @MappingTarget VsTopic vsTopic);
}
