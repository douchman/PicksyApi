package com.buck.vsplay.domain.vstopic.service.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicServiceHelper {

    public static boolean isThumbnailUpdated(String thumbnail){
        return thumbnail != null && !thumbnail.isEmpty();
    }
}
