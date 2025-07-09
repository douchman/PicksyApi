package com.buck.vsplay.domain.vstopic.service.support;

import com.buck.vsplay.global.constants.Visibility;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicServiceHelper {

    public static boolean isThumbnailUpdated(String thumbnail){
        return thumbnail != null && !thumbnail.isEmpty();
    }

    public static boolean isMissingPasswordForProtectedTopic(Visibility visibility, String accessCode){
        return visibility == Visibility.PASSWORD && (accessCode == null || accessCode.isBlank());
    }
}
