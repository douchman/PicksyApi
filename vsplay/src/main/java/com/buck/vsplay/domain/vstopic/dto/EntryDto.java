package com.buck.vsplay.domain.vstopic.dto;


import com.buck.vsplay.global.constants.MediaType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryDto {

    @Data
    public static class Entry{
        private String entryName;
        private String description;
        private MultipartFile file;
    }

    @Data
    public static class CreatedEntry{
        private Long entryId;
        private String entryName;
        private String description;
        private MediaType mediaType;
        private String mediaUrl;
    }

    @Data
    public static class CreateEntriesRequest{
        Long topicId;
        List<Entry> entries;
    }

    @Data
    public static class CreatedEntryList{
        List<CreatedEntry> entries;
    }
}
