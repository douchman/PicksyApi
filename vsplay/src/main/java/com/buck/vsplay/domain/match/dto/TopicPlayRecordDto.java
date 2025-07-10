package com.buck.vsplay.domain.match.dto;


import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicPlayRecordDto {

    @Getter
    @Setter
    public static class PlayRecordRequest{
        Integer tournamentStage;
        String accessCode;
    }

    @Getter
    @Setter
    @Builder
    public static class PlayRecordResponse{
        Long playRecordId;
    }

}
