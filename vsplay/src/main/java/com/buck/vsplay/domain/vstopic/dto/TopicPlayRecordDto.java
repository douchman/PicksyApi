package com.buck.vsplay.domain.vstopic.dto;


import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicPlayRecordDto {

    @Data
    public static class PlayRecordRequest{
        Integer tournamentStage;
        String accessCode;
    }

    @Data
    @AllArgsConstructor
    public static class PlayRecordResponse{
        Long playRecordId;
    }

}
