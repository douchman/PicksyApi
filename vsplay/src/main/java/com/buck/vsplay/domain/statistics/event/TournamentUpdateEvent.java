package com.buck.vsplay.domain.statistics.event;


import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TournamentUpdateEvent {
    private final TopicTournament topicTournament;
}
