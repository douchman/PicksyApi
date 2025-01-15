package com.buck.vsplay.domain.vstopic.dto;


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
    public static class createEntriesRequest{
        Integer topicId;
        List<Entry> entries;
    }
}
