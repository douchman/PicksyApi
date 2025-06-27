package com.buck.vsplay.domain.vstopic.service.impl;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.statistics.event.EntryEvent;
import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.entry.EntryException;
import com.buck.vsplay.domain.vstopic.exception.entry.EntryExceptionCode;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.mapper.TopicEntryMapper;
import com.buck.vsplay.domain.vstopic.repository.EntryRepository;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.domain.vstopic.service.IEntryService;
import com.buck.vsplay.domain.vstopic.service.support.TournamentHandler;
import com.buck.vsplay.global.constants.MediaType;
import com.buck.vsplay.global.constants.ModerationStatus;
import com.buck.vsplay.global.security.service.impl.AuthUserService;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import com.buck.vsplay.global.util.gpt.client.BadWordFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EntryService implements IEntryService {

    private final VsTopicRepository topicRepository;
    private final TopicEntryMapper topicEntryMapper;
    private final AuthUserService authUserService;
    private final EntryRepository entryRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final BadWordFilter badWordFilter;
    private final TournamentHandler tournamentHandler;
    private final S3Util s3Util;

    @Override
    public EntryDto.EntryList getEntriesByTopicId(Long topicId) {
        EntryDto.EntryList entryList = new EntryDto.EntryList();

        if(!topicRepository.existsByIdAndDeletedFalse(topicId)) {
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND);
        }

        List<TopicEntry> createdEntries = entryRepository.findByTopicIdAndDeletedFalse(topicId);

        if( createdEntries != null && !createdEntries.isEmpty()){
            for (TopicEntry createdEntry : createdEntries) {

                boolean isYoutube = MediaType.YOUTUBE == createdEntry.getMediaType();

                entryList.getEntries().add(
                        isYoutube ?
                                topicEntryMapper.toEntryDtoFromEntityWithoutSignedMediaUrl(createdEntry, s3Util)
                                : topicEntryMapper.toEntryDtoFromEntity(createdEntry, s3Util)
                );
            }
        }

        return entryList;
    }

    @Override
    public void createEntries(Long topicId, EntryDto.CreateEntriesRequest request) {
        Member authUser = authUserService.getAuthUser();

        VsTopic vsTopic = topicRepository.findById(topicId).orElseThrow(() ->
                new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));

        if(!vsTopic.getMember().getId().equals(authUser.getId())){
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_CREATOR_ONLY);
        }

        List<EntryDto.CreateEntry> entries = request.getEntries();

        List<String> textsForBadWordFilter = new ArrayList<>();
        for(EntryDto.CreateEntry entry : entries){
            textsForBadWordFilter.addAll(buildStringList(entry.getEntryName(), entry.getDescription()));
        }

        boolean hasBadWord = badWordFilter.containsBadWords(textsForBadWordFilter);
        if(hasBadWord){
            throw new EntryException(EntryExceptionCode.BAD_WORD_DETECTED);
        }

        List<TopicEntry> topicEntries = new ArrayList<>();

        for (EntryDto.CreateEntry entry : entries) {
            TopicEntry topicEntry = topicEntryMapper.toEntityFromCreatedEntryDto(entry);
            topicEntry.setTopic(vsTopic);
            topicEntry.setModerationStatus(ModerationStatus.PASSED);
            topicEntries.add(topicEntry); // DTO -> Entity 매핑
        }

        entryRepository.saveAll(topicEntries);
        applicationEventPublisher.publishEvent(new EntryEvent.CreateEvent(topicEntries));
        tournamentHandler.handleTournament(vsTopic);
    }

    @Override
    public void updateEntries(Long topicId, EntryDto.UpdateEntryRequest updatedRequest) {
        Member authUser = authUserService.getAuthUser();

        VsTopic topic = topicRepository.findByIdAndDeletedFalse(topicId).orElseThrow(() ->
            new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND)
        );

        if(!topic.getMember().getId().equals(authUser.getId())){
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_CREATOR_ONLY);
        }

        List<EntryDto.UpdateEntry> entriesToUpdate = Optional
                .ofNullable(updatedRequest.getEntriesToUpdate())
                .orElse(Collections.emptyList());

        if( entriesToUpdate.isEmpty() ) return;

        List<Long> updateTargetEntryIds = entriesToUpdate.stream()
                .map(EntryDto.UpdateEntry::getId)
                .toList();

        List<String> textsForBadWordFilter = new ArrayList<>();
        for(EntryDto.UpdateEntry entry : entriesToUpdate){
            textsForBadWordFilter.addAll(buildStringList(entry.getEntryName(), entry.getDescription()));
        }

        boolean hasBadWord = badWordFilter.containsBadWords(textsForBadWordFilter);
        if(hasBadWord){
            throw new EntryException(EntryExceptionCode.BAD_WORD_DETECTED);
        }

        List<TopicEntry> existingEntries = entryRepository.findByTopicIdAndIdIn(topicId, updateTargetEntryIds);

        Map<Long, TopicEntry> entryMap = existingEntries.stream()
                .collect(Collectors.toMap(TopicEntry::getId, Function.identity()));

        for (EntryDto.UpdateEntry updateRequestEntry : entriesToUpdate) {
            Optional.ofNullable(entryMap.get(updateRequestEntry.getId()))
                    .ifPresent(existingEntry -> {
                        if (updateRequestEntry.isDelete()) {
                            handleDeleteEntry(existingEntry);
                        } else {
                            handleUpdateEntry(existingEntry, updateRequestEntry);
                        }
                    });
        }
        tournamentHandler.handleTournament(topic);
    }


    private void handleDeleteEntry(TopicEntry existingEntry) {
        existingEntry.setDeleted(true);
    }

    private void handleUpdateEntry(TopicEntry existingEntry, EntryDto.UpdateEntry updateRequestEntry) {

        if( updateRequestEntry.getEntryName() != null ){
            existingEntry.setEntryName(updateRequestEntry.getEntryName());
        }

        if( updateRequestEntry.getDescription() != null ){
            existingEntry.setDescription(updateRequestEntry.getDescription());
        }

        if( isEntryMediaUpdated(updateRequestEntry.getMediaUrl())){
            existingEntry.setMediaType(updateRequestEntry.getMediaType());
            existingEntry.setMediaUrl(updateRequestEntry.getMediaUrl());
        }

        if(isThumbnailUpdated(updateRequestEntry.getThumbnail())){
            existingEntry.setThumbnail(updateRequestEntry.getThumbnail());
        }
    }

    private List<String> buildStringList(String ... strings) {
        return List.of(strings);
    }

    private boolean isEntryMediaUpdated(String mediaUrl){
        return mediaUrl != null && !mediaUrl.isEmpty();
    }

    private boolean isThumbnailUpdated(String thumbnail){
        return thumbnail != null && !thumbnail.isEmpty();
    }
}
