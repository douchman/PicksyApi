package com.buck.vsplay.domain.entry.service.impl;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.statistics.event.EntryEvent;
import com.buck.vsplay.domain.entry.dto.EntryDto;
import com.buck.vsplay.domain.entry.entiity.TopicEntry;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.entry.mapper.TopicEntryMapper;
import com.buck.vsplay.domain.vstopic.moderation.TopicAccessGuard;
import com.buck.vsplay.domain.entry.repository.EntryRepository;
import com.buck.vsplay.domain.entry.service.IEntryService;
import com.buck.vsplay.domain.entry.service.checker.EntryRequestChecker;
import com.buck.vsplay.domain.vstopic.service.finder.TopicFinder;
import com.buck.vsplay.domain.entry.service.handler.EntryUpdateHandler;
import com.buck.vsplay.domain.entry.service.support.EntryTextExtractor;
import com.buck.vsplay.domain.entry.service.support.TournamentHandler;
import com.buck.vsplay.global.constants.MediaType;
import com.buck.vsplay.global.constants.ModerationStatus;
import com.buck.vsplay.global.security.service.impl.AuthUserService;
import com.buck.vsplay.global.util.aws.s3.S3Util;
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

    private final TopicEntryMapper topicEntryMapper;
    private final AuthUserService authUserService;
    private final EntryRepository entryRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TournamentHandler tournamentHandler;
    private final S3Util s3Util;
    private final EntryUpdateHandler entryUpdateHandler;
    private final TopicFinder topicFinder;
    private final EntryRequestChecker entryRequestChecker;
    private final EntryTextExtractor entryTextExtractor;

    @Override
    public EntryDto.EntryList getEntriesByTopicId(Long topicId) {

        topicFinder.validateTopicExists(topicId);

        List<EntryDto.Entry> entryList = new ArrayList<>();
        List<TopicEntry> createdEntries = entryRepository.findByTopicIdAndDeletedFalse(topicId);

        if( createdEntries != null && !createdEntries.isEmpty()){
            for (TopicEntry createdEntry : createdEntries) {

                boolean isYoutube = MediaType.YOUTUBE == createdEntry.getMediaType();

                entryList.add(
                        isYoutube ?
                                topicEntryMapper.toEntryDtoFromEntityWithoutSignedMediaUrl(createdEntry, s3Util)
                                : topicEntryMapper.toEntryDtoFromEntity(createdEntry, s3Util)
                );
            }
        }

        return EntryDto.EntryList.builder()
                .entries(entryList)
                .build();
    }

    @Override
    public void createEntries(Long topicId, EntryDto.CreateEntriesRequest request) {
        Member member = authUserService.getAuthUser();

        VsTopic vsTopic = topicFinder.findExistingById(topicId);
        TopicAccessGuard.validateTopicAccess(vsTopic, member);
        entryRequestChecker.checkEntryCreateRequest(request);

        List<TopicEntry> topicEntries = new ArrayList<>();
        for (EntryDto.CreateEntry entry : request.getEntriesToCreate()) {
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
        Member member = authUserService.getAuthUser();

        VsTopic vsTopic = topicFinder.findExistingById(topicId);
        TopicAccessGuard.validateTopicAccess(vsTopic, member);


        List<EntryDto.UpdateEntry> entriesToUpdate = Optional
                .ofNullable(updatedRequest.getEntriesToUpdate())
                .orElse(Collections.emptyList()); // null safe 하게 리스트 재구성

        if( entriesToUpdate.isEmpty() ) return; // early return


        List<Long> updateTargetEntryIds = entriesToUpdate.stream()
                .map(EntryDto.UpdateEntry::getId)
                .toList(); // 업데이트 대상 entry id 추출

        entryRequestChecker.filterEntriesContentTexts(entryTextExtractor.extractTextFromUpdateEntryList(entriesToUpdate)); // 텍스트 컨텐츠 추출 후 비속어 필터링 ( 삭제 대상 제외 )

        List<TopicEntry> existingEntries = entryRepository.findByTopicIdAndIdIn(topicId, updateTargetEntryIds); // entity 조회

        Map<Long, TopicEntry> entryMap = existingEntries.stream()
                .collect(Collectors.toMap(TopicEntry::getId, Function.identity())); // 리스트 순회 처리에 용이하도록 Map 으로 Structure 재구성

        for (EntryDto.UpdateEntry updateRequestEntry : entriesToUpdate) {
            Optional.ofNullable(entryMap.get(updateRequestEntry.getId()))
                    .ifPresent(existingEntry -> {
                        if (updateRequestEntry.isDelete()) { // 삭제
                            entryUpdateHandler.handleDeleteEntry(existingEntry);
                        } else { // 업데이트
                            entryUpdateHandler.handleUpdateEntry(existingEntry, updateRequestEntry);
                        }
                    });
        }
        tournamentHandler.handleTournament(vsTopic); // 갱신된 엔트리를 기준으로 토너먼트 재구성
    }
}
