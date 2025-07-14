package com.buck.vsplay.domain.entry.dto;


import com.buck.vsplay.global.constants.MediaType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryDto {

    @Getter
    @Setter
    @Builder
    public static class Entry{
        private Long id;
        private String entryName;
        private String description;
        private MediaType mediaType;
        private String mediaUrl;
        private String thumbnail;
    }

    @Getter
    @Setter
    @Builder
    public static class CreateEntry{
        private String entryName;
        private String description;
        private String mediaUrl; // 파일 또는 유튜브
        private String thumbnail; // 썸네일
        private MediaType mediaType;
    }

    @Getter
    @Setter
    @Builder
    public static class UpdateEntry{
        private Long id;
        private String entryName;
        private String description;
        private String mediaUrl;
        private String thumbnail;
        private MediaType mediaType;
        private boolean delete;
    }

    @Getter
    @Setter
    public static class CreateEntriesRequest{
        List<CreateEntry> entriesToCreate;
    }

    @Getter
    @Setter
    @Builder
    public static class EntryList{
        List<Entry> entries;
    }

    @Getter
    @Setter
    public static class UpdateEntryRequest{
        List<UpdateEntry> entriesToUpdate;
    }
}
