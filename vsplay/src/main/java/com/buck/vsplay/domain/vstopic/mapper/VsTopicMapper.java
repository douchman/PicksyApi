package com.buck.vsplay.domain.vstopic.mapper;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VsTopicMapper {
    @Mapping(target = "thumbnail", ignore = true)
    VsTopic toEntity(VsTopicDto.VsTopicCreateRequest vsTopicCreateRequest);

    @Mapping(target = "thumbnail", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVsTopicFromUpdateRequest(VsTopicDto.VsTopicUpdateRequest vsTopicUpdateRequest, @MappingTarget VsTopic vsTopic);
}
