package com.buck.vsplay.domain.vstopic.dto;


import com.buck.vsplay.global.constants.MediaType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryDto {

    @Data
    public static class Entry{
        private Long id;
        private String entryName;
        private String description;
        private MediaType mediaType;
        private String mediaUrl;
        private String thumbnail;
    }

    @Data
    public static class CreateEntry{
        private String entryName;
        private String description;
        private String mediaUrl; // 파일 또는 유튜브
        private String thumbnail; // 썸네일
        private MediaType mediaType;
    }

    @Data
    public static class UpdateEntry{
        private Long id;
        private String entryName;
        private String description;
        private String mediaUrl;
        private String thumbnail;
        private MediaType mediaType;
        private boolean delete = false;
    }

    @Data
    public static class CreateEntriesRequest{
        Long topicId;
        List<CreateEntry> entries;
    }

    @Data
    public static class EntryList{
        List<Entry> entries = new ArrayList<>();
    }

    @Data
    public static class UpdateEntryRequest{
        List<UpdateEntry> entriesToUpdate;
    }
}
