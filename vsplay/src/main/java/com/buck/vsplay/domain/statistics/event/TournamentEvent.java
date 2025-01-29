package com.buck.vsplay.domain.statistics.event;


import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TournamentEvent {


    @Data
    @AllArgsConstructor
    public static class CreateEvent{
        TopicTournament topicTournament;
    }
}
