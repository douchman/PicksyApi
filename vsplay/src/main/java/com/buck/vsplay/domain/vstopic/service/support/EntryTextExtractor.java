package com.buck.vsplay.domain.vstopic.service.support;

import com.buck.vsplay.domain.entry.dto.EntryDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EntryTextExtractor {

    public List<String> extractTextFromUpdateEntryList(List<EntryDto.UpdateEntry> entriesToUpdate){
        List<String> textsForBadWordFilter = new ArrayList<>();
        for(EntryDto.UpdateEntry entry : entriesToUpdate){
            if( !entry.isDelete())
                textsForBadWordFilter.addAll(FilterTextListBuilder.buildStringList(entry.getEntryName(), entry.getDescription()));
        }

        return textsForBadWordFilter;
    }
}
