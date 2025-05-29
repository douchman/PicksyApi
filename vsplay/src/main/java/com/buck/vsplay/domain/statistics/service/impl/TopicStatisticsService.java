package com.buck.vsplay.domain.statistics.service.impl;

import com.buck.vsplay.domain.statistics.dto.TopicStatisticsDto;
import com.buck.vsplay.domain.statistics.entity.TopicStatistics;
import com.buck.vsplay.domain.statistics.event.TopicEvent;
import com.buck.vsplay.domain.statistics.mapper.TopicStatisticsMapper;
import com.buck.vsplay.domain.statistics.projection.MostPopularEntry;
import com.buck.vsplay.domain.statistics.repository.TopicStatisticsRepository;
import com.buck.vsplay.domain.statistics.service.ITopicStatisticsService;
import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.mapper.VsTopicMapper;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.global.constants.MediaType;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TopicStatisticsService implements ITopicStatisticsService {

    private final TopicStatisticsRepository topicStatisticsRepository;
    private final VsTopicRepository vsTopicRepository;
    private final TopicStatisticsMapper topicStatisticsMapper;
    private final VsTopicMapper vsTopicMapper;
    private final S3Util s3Util;

    @EventListener
    public void handleVsTopicCreated(TopicEvent.CreateEvent topiCreateEvent) {
        createTopicStatistics(topiCreateEvent.getTopic());
    }

    @EventListener
    public void handleVsTopicPlayed(TopicEvent.PlayEvent topiPlayEvent) {
        recordPlayStats(topiPlayEvent.getTopic());
    }


    @EventListener
    public void handleVsTopicPlayRecordCompleted(TopicEvent.PlayCompleteEvent topiPlayCompleteEvent) {
        recordCompletedMatchStats(topiPlayCompleteEvent.getTopic());
    }

    @Override
    public void createTopicStatistics(VsTopic vsTopic) {
        topicStatisticsRepository.save(TopicStatistics.builder()
                .vsTopic(vsTopic)
                .totalMatches(0)
                .completedMatches(0)
                .build());
    }

    @Override
    public void recordPlayStats(VsTopic vsTopic) {
        TopicStatistics topicStatistics = topicStatisticsRepository.findByVsTopic(vsTopic.getId());

        topicStatistics.increaseTotalMatches();
        topicStatistics.updatePlayedDates();

        topicStatisticsRepository.save(topicStatistics);
    }

    @Override
    public void recordCompletedMatchStats(VsTopic vsTopic) {
        TopicStatistics topicStatistics = topicStatisticsRepository.findByVsTopic(vsTopic.getId());

        List<MostPopularEntry> mostPopularEntries = topicStatisticsRepository.findMostPopularEntriesByTopicId(vsTopic.getId()); // 결승전에서 승리한 엔트리와 승리 횟수 조회
        MostPopularEntry mostPopularEntry = mostPopularEntries.get(0); // 가장 높은 승리 횟수를 기록한 엔트리 선별

        topicStatistics.increaseCompletedMatches();
        topicStatistics.setMostPopularEntry(mostPopularEntry.getEntry());

        topicStatisticsRepository.save(topicStatistics);
    }

    @Override
    public TopicStatisticsDto.TopicStatisticsResponse getTopicStatistics(Long topicId) {

        if(!vsTopicRepository.existsByIdAndDeletedFalse(topicId)) {
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND);
        }

        TopicStatistics topicStatisticsEntity = topicStatisticsRepository.findByVsTopic(topicId);
        TopicStatisticsDto.TopicStatistics topicStatistics = topicStatisticsMapper.toTopicStatisticsDtoFromEntity(topicStatisticsEntity);
        VsTopicDto.VsTopic vsTopic = vsTopicMapper.toVsTopicDtoFromEntity(topicStatisticsEntity.getVsTopic());

        EntryDto.Entry mostPopularEntry = topicStatistics.getMostPopularEntry();
        if( mostPopularEntry != null ) {
            boolean isMediaTypeYoutube = MediaType.YOUTUBE == mostPopularEntry.getMediaType();

            if( !isMediaTypeYoutube ){ // 유튜브인 경우 signedUrl 변환 없음
                mostPopularEntry.setMediaUrl(s3Util.getUploadedObjectUrl(mostPopularEntry.getMediaUrl()));
            }
        }

        return TopicStatisticsDto.TopicStatisticsResponse.builder()
                .topic(vsTopic)
                .topicStatistics(topicStatistics)
                .build();
    }
}
