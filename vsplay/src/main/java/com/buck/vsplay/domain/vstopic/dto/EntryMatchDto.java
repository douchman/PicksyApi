package com.buck.vsplay.domain.vstopic.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryMatchDto {

    @Data
    public static class EntryMatch{
        EntryDto.Entry entryA;
        EntryDto.Entry entryB;
    }

    @Data
    public static class EntryMatchResponse{
        Long matchId;
        EntryMatch entryMatch = new EntryMatch();
    }
}
