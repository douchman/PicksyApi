package com.buck.vsplay.domain.vstopic.mapper;


import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = S3Util.class)
public interface TopicEntryMapper {

    @Mapping(target = "entryId" , source = "id")
    @Mapping(target = "mediaUrl" , qualifiedByName = "signedMediaUrl")
    EntryDto.Entry toTopicEntryDtoFromEntity(TopicEntry topicEntry);

    @Mapping(target = "topic", expression = "java(vsTopic)")
    TopicEntry toTopicEntryWithTopic(EntryDto.CreateEntry topicEntry, @Context VsTopic vsTopic);

    @Mapping(source = "id", target ="entryId")
    @Mapping(target = "mediaUrl" , qualifiedByName = "signedMediaUrl")
    EntryDto.Entry toCreatedEntry(TopicEntry topicEntry);

    @Mapping(source = "id", target ="entryId")
    @Mapping(target = "mediaUrl", qualifiedByName = "signedMediaUrl")
    EntryDto.Entry toEntryDtoFromEntity(TopicEntry topicEntry);

    @Named("signedMediaUrl")
    default String signedMediaUrl(String mediaUrl, @Context S3Util s3Util) {
        return s3Util.getUploadedObjectUrl(mediaUrl);
    }


    default List<EntryDto.Entry> toCreatedEntryList(List<TopicEntry> entries){
        return entries.stream()
                .map(this::toCreatedEntry)
                .toList();
    }

    /**
     * 엔티티 업데이트를 위해 DTO 의 값을 기존 Entry 엔티티에 적용
     * Null 필드는 자동으로 제외
     * @param updatedRequest  기존 엔트리에 업데이트 할 변경된 엔트리 값
     * @param topicEntry 업데이트를 적용할 기존의 엔트리
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void applyChangesToTopicEntry(EntryDto.UpdateEntryRequest updatedRequest, @MappingTarget TopicEntry topicEntry);

}
