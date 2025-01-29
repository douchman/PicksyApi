package com.buck.vsplay.domain.statistics.event;

import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicEvent{


    @Data
    @AllArgsConstructor
    public static class CreateEvent{
        VsTopic topic;
    }
}
