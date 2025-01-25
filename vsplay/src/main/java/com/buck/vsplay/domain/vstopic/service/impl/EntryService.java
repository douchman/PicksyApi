package com.buck.vsplay.domain.vstopic.service.impl;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.statistics.event.TournamentUpdateEvent;
import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.entry.EntryException;
import com.buck.vsplay.domain.vstopic.exception.entry.EntryExceptionCode;
import com.buck.vsplay.domain.vstopic.exception.tournament.TournamentException;
import com.buck.vsplay.domain.vstopic.exception.tournament.TournamentExceptionCode;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


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
    public EntryDto.CreatedEntryList getEntriesByTopicId(Long topicId) {
        EntryDto.CreatedEntryList createdEntryList = new EntryDto.CreatedEntryList();

        if(!topicRepository.existsById(topicId)) {
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND);
        }

        createdEntryList.setEntries(topicEntryMapper.toCreatedEntryList(entryRepository.findByTopicId(topicId)));

        return createdEntryList;
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
            S3Dto.S3UploadResult s3UploadResult = s3Util.putObject(entry.getFile(), objectPath);
            TopicEntry topicEntry = topicEntryMapper.toTopicEntryWithTopic(entry, vsTopic);
            topicEntry.setMediaUrl(s3UploadResult.getObjectKey());
            topicEntry.setMediaType(s3UploadResult.getMediaType());
            topicEntries.add(topicEntry); // DTO -> Entity 매핑
        }

        entryRepository.saveAll(topicEntries);
        updateTopicTournament(vsTopic);
    }

    @Override
    public void createDummyEntries(Long topicId) {
        int dummyCount = 32;
        VsTopic vsTopic = topicRepository.findById(topicId).orElseThrow(() ->
                new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));


        List<TopicEntry> topicEntries = new ArrayList<>();

        for (int i = 0; i < dummyCount; i++) {
            TopicEntry topicEntry = new TopicEntry();
            topicEntry.setTopic(vsTopic);
            topicEntry.setEntryName("더미 엔트리" + (i+1));
            topicEntry.setDescription("더미 엔트리 설명 " + (i+1));
            topicEntry.setMediaUrl("1/1/c89ba7dbebb64aa297a9ec063644470e.png");
            topicEntry.setMediaType(MediaType.IMAGE);
            topicEntries.add(topicEntry); // DTO -> Entity 매핑
        }

        entryRepository.saveAll(topicEntries);
        updateTopicTournament(vsTopic);
    }

    @Override
    public void updateEntries(Long topicId, Long entryId, EntryDto.UpdateEntryRequest updatedRequest) {

        Member authUser = authUserService.getAuthUser();
        if( !topicRepository.existsById(topicId) ) {
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND);
        }

        TopicEntry topicEntry = entryRepository.findById(entryId).orElseThrow(
                () -> new EntryException(EntryExceptionCode.ENTRY_NOT_FOUND));

        topicEntryMapper.applyChangesToTopicEntry(updatedRequest, topicEntry);

        MultipartFile uploadFile = updatedRequest.getFile();
        boolean isFileExist = (uploadFile != null && !uploadFile.isEmpty());

        if(isFileExist){
            String objectKey = s3Util.buildS3Path(String.valueOf(authUser.getId()), String.valueOf(topicId));
            S3Dto.S3UploadResult s3UploadResult = s3Util.putObject(uploadFile, objectKey);

            topicEntry.setMediaUrl(s3UploadResult.getObjectKey());
            topicEntry.setMediaType(s3UploadResult.getMediaType());
        }

        entryRepository.save(topicEntry);
    }

    private void updateTopicTournament(VsTopic vsTopic) {
        final int INITIAL_TOURNAMENT_STAGE = 2;
        List<TopicEntry> topicEntries = entryRepository.findByTopicId(vsTopic.getId());

        boolean isEntryExist = (topicEntries != null && !topicEntries.isEmpty());

        if(!isEntryExist) { // 엔트리가 존재하지 않으면 처리하지 않음
            return;
        }

        int power = 1;
        int entryCount = topicEntries.size();

        while ( true ) { // 사용가능 토너먼트 계산 및 저장
            int tournamentStage = (int)Math.pow(INITIAL_TOURNAMENT_STAGE, power);

            if( tournamentStage > entryCount){
                break;
            }

            if( !isTournamentExist(vsTopic, tournamentStage) ) {
                TopicTournament saveTournament = saveTournament(vsTopic, tournamentStage); // 토너먼트 엔티티 커밋
                applicationEventPublisher.publishEvent(new TournamentUpdateEvent(saveTournament)); // 커밋된 엔티티로 이벤트 발행
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
        topicTournament.setTournamentName(TournamentStage.findStageNameByStage(tournamentStage).orElseThrow( () ->
                new TournamentException(TournamentExceptionCode.SERVER_ERROR)));
        topicTournament.setTournamentStage(tournamentStage);
        return tournamentRepository.save(topicTournament);
    }
}
