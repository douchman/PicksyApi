package com.buck.vsplay.domain.statistics.projection;

import com.buck.vsplay.domain.entry.entiity.TopicEntry;

public interface MostPopularEntry {
    TopicEntry getEntry();
    int getWinCount();
}
