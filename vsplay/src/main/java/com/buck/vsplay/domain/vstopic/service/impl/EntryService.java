package com.buck.vsplay.domain.vstopic.service.impl;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.statistics.event.EntryEvent;
import com.buck.vsplay.domain.statistics.event.TournamentEvent;
import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.mapper.TopicEntryMapper;
import com.buck.vsplay.domain.vstopic.repository.EntryRepository;
import com.buck.vsplay.domain.vstopic.repository.TournamentRepository;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.domain.vstopic.service.IEntryService;
import com.buck.vsplay.global.constants.MediaType;
import com.buck.vsplay.global.constants.TournamentStage;
import com.buck.vsplay.global.security.service.impl.AuthUserService;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import com.buck.vsplay.global.util.aws.s3.dto.S3Dto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EntryService implements IEntryService {

    private final S3Util s3Util;
    private final VsTopicRepository topicRepository;
    private final TopicEntryMapper topicEntryMapper;
    private final AuthUserService authUserService;
    private final EntryRepository entryRepository;
    private final TournamentRepository tournamentRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

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
                                topicEntryMapper.toEntryDtoFromEntityWithoutSignedMediaUrl(createdEntry)
                                : topicEntryMapper.toEntryDtoFromEntity(createdEntry)
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


        List<EntryDto.CreateEntry> entries = request.getEntries();
        List<TopicEntry> topicEntries = new ArrayList<>();
        String objectPath = s3Util.buildS3Path(String.valueOf(authUser.getId()), String.valueOf(vsTopic.getId()));

        for (EntryDto.CreateEntry entry : entries) {
            TopicEntry topicEntry = topicEntryMapper.toEntityFromCreatedEntryDto(entry);
            topicEntry.setTopic(vsTopic);
            // 미디어 파일 & 썸네일 업로드
            if ( entry.getMediaFile() != null && !entry.getMediaFile().isEmpty()) {
                S3Dto.S3UploadResult mediaFileUploadResult = s3Util.putObject(entry.getMediaFile(), objectPath);
                topicEntry.setMediaUrl(mediaFileUploadResult.getObjectKey());
                topicEntry.setMediaType(mediaFileUploadResult.getMediaType());
            }else{
                topicEntry.setMediaType(MediaType.YOUTUBE);
            }

            // 썸네일 존재 시 썸네일도 업로드
            if( entry.getThumbnailFile() != null && !entry.getThumbnailFile().isEmpty()) {
                S3Dto.S3UploadResult thumbFileUploadResult = s3Util.putObject(entry.getThumbnailFile(), objectPath);
                topicEntry.setThumbnail(thumbFileUploadResult.getObjectKey());
            }

            topicEntries.add(topicEntry); // DTO -> Entity 매핑
        }

        entryRepository.saveAll(topicEntries);
        applicationEventPublisher.publishEvent(new EntryEvent.CreateEvent(topicEntries));
        updateTopicTournament(vsTopic);
    }

    @Override
    public void updateEntries(Long topicId, EntryDto.UpdateEntryRequest updatedRequest) {

        Member authUser = authUserService.getAuthUser();
        if( !topicRepository.existsById(topicId) ) {
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND);
        }

        List<EntryDto.UpdateEntry> entriesToUpdate = Optional
                .ofNullable(updatedRequest.getEntriesToUpdate())
                .orElse(Collections.emptyList());

        if( entriesToUpdate.isEmpty() ) return;

        List<Long> updateTargetEntryIds = entriesToUpdate.stream()
                .map(EntryDto.UpdateEntry::getId)
                .toList();

        List<TopicEntry> existingEntries = entryRepository.findByTopicIdAndIdIn(topicId, updateTargetEntryIds);

        Map<Long, TopicEntry> entryMap = existingEntries.stream()
                .collect(Collectors.toMap(TopicEntry::getId, Function.identity()));

        // S3 업로드 경로
        String objectPath = s3Util.buildS3Path(String.valueOf(authUser.getId()), String.valueOf(topicId));

        for (EntryDto.UpdateEntry updateRequestEntry : entriesToUpdate) {
            Optional.ofNullable(entryMap.get(updateRequestEntry.getId()))
                    .ifPresent(existingEntry -> {
                        if (updateRequestEntry.isDelete()) {
                            handleDeleteEntry(existingEntry);
                        } else {
                            handleUpdateEntry(existingEntry, updateRequestEntry, objectPath);
                        }
                    });
        }
    }

    private void updateTopicTournament(VsTopic vsTopic) {
        log.info(" @@@@@@@@@@@@@@@@@@@@@@@@@@@@ 토픽 토너먼트 @@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        final int INITIAL_TOURNAMENT_STAGE = 2;
        List<TopicEntry> topicEntries = entryRepository.findByTopicIdAndDeletedFalse(vsTopic.getId());

        boolean isEntryExist = (topicEntries != null && !topicEntries.isEmpty());

        if(!isEntryExist) { // 엔트리가 존재하지 않으면 처리하지 않음
            log.info(" 엔트리가 없어서 토너먼트를 추가하지 않습니다");
            return;
        }

        int power = 1;
        int entryCount = topicEntries.size();
        log.info("entryCount  ->>> {} ", entryCount );
        while ( true ) { // 사용가능 토너먼트 계산 및 저장
            int tournamentStage = (int)Math.pow(INITIAL_TOURNAMENT_STAGE, power);

            log.info("tournamentStage ! ->>> {} ", tournamentStage );
            if( tournamentStage > entryCount){
                break;
            }

            if( !isTournamentExist(vsTopic, tournamentStage) ) {
                log.info("토너먼트가 없네요? 추가해볼게요");
                TopicTournament saveTournament = saveTournament(vsTopic, tournamentStage); // 토너먼트 엔티티 커밋
                applicationEventPublisher.publishEvent(new TournamentEvent.CreateEvent(saveTournament)); // 커밋된 엔티티로 이벤트 발행
            }

            power++;
        }
    }

    private boolean isTournamentExist(VsTopic vsTopic, int tournamentStage) {
        return tournamentRepository.existsByVsTopicIdAndTournamentStage(vsTopic.getId(), tournamentStage);

    }

    private TopicTournament saveTournament(VsTopic vsTopic, int tournamentStage) {

        TopicTournament topicTournament = new TopicTournament();
        topicTournament.setVsTopic(vsTopic);
        topicTournament.setTournamentName(TournamentStage.findStageNameByStage(tournamentStage));
        topicTournament.setTournamentStage(tournamentStage);
        return tournamentRepository.save(topicTournament);
    }


    private void handleDeleteEntry(TopicEntry existingEntry) {
        existingEntry.setDeleted(true);
    }

    private void handleUpdateEntry(TopicEntry existingEntry, EntryDto.UpdateEntry updateRequestEntry, String objectPath) {
        existingEntry.setEntryName(updateRequestEntry.getEntryName());
        existingEntry.setDescription(updateRequestEntry.getDescription());

        if( updateRequestEntry.getMediaFile() != null && !updateRequestEntry.getMediaFile().isEmpty()) {
            S3Dto.S3UploadResult mediaFileUploadResult = s3Util.putObject(updateRequestEntry.getMediaFile(), objectPath);
            existingEntry.setMediaUrl(mediaFileUploadResult.getObjectKey());
            existingEntry.setMediaType(mediaFileUploadResult.getMediaType());
            existingEntry.setThumbnail(null); // 썸네일 비우기
        } else {
            existingEntry.setMediaUrl(updateRequestEntry.getMediaUrl());
            existingEntry.setMediaType(MediaType.YOUTUBE);
        }

        // 썸네일 존재 시 썸네일도 업로드 ( VIDEO & YOUTUBE )
        if( updateRequestEntry.getThumbnailFile() != null && !updateRequestEntry.getThumbnailFile().isEmpty()) {
            S3Dto.S3UploadResult thumbFileUploadResult = s3Util.putObject(updateRequestEntry.getThumbnailFile(), objectPath);
            existingEntry.setThumbnail(thumbFileUploadResult.getObjectKey());
        }
    }
}
