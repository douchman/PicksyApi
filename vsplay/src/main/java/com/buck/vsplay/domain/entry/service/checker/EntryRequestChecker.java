package com.buck.vsplay.domain.entry.service.checker;


import com.buck.vsplay.domain.entry.dto.EntryDto;
import com.buck.vsplay.domain.entry.exception.EntryException;
import com.buck.vsplay.domain.entry.exception.EntryExceptionCode;
import com.buck.vsplay.domain.vstopic.service.support.FilterTextListBuilder;
import com.buck.vsplay.global.util.gpt.client.BadWordFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EntryRequestChecker {

    private final BadWordFilter badWordFilter;

    public void checkEntryCreateRequest(EntryDto.CreateEntriesRequest request){
        List<EntryDto.CreateEntry> entries = request.getEntriesToCreate();

        List<String> textsForBadWordFilter = new ArrayList<>();

        for(EntryDto.CreateEntry entry : entries){
            textsForBadWordFilter.addAll(FilterTextListBuilder.buildStringList(entry.getEntryName(), entry.getDescription()));
        }

        filterEntriesContentTexts(textsForBadWordFilter);
    }

    public void filterEntriesContentTexts(List<String> textsForBadWordFilter){

        if(textsForBadWordFilter == null || textsForBadWordFilter.isEmpty()) return;

        boolean hasBadWord = badWordFilter.containsBadWords(textsForBadWordFilter);
        if(hasBadWord){
            throw new EntryException(EntryExceptionCode.BAD_WORD_DETECTED);
        }
    }
}
