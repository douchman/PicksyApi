package com.buck.vsplay.domain.vstopic.dto;


import com.buck.vsplay.global.constants.MediaType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
        private String mediaUrl;
        private MultipartFile mediaFile;
        private MultipartFile thumbnailFile;
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

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class UpdateEntryRequest extends CreateEntry{}
}
