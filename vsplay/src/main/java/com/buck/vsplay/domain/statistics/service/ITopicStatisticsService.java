package com.buck.vsplay.domain.statistics.service;

import com.buck.vsplay.domain.vstopic.entity.VsTopic;

public interface ITopicStatisticsService {
    void createTopicStatistics(VsTopic vsTopic);
    void recordPlayStats(VsTopic vsTopic);
    void recordCompletedMatchStats(VsTopic vsTopic);
}
