package com.buck.vsplay.domain.statistics.projection;

import com.buck.vsplay.domain.vstopic.entity.TopicEntry;

public interface MostPopularEntry {
    TopicEntry getEntry();
    int getWinCount();
}
