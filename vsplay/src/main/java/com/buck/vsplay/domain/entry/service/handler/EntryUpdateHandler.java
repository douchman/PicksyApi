package com.buck.vsplay.domain.entry.service.handler;

import com.buck.vsplay.domain.entry.dto.EntryDto;
import com.buck.vsplay.domain.entry.entiity.TopicEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntryUpdateHandler {

    public void handleDeleteEntry(TopicEntry existingEntry) {
        existingEntry.setDeleted(true);
    }

    public void handleUpdateEntry(TopicEntry existingEntry, EntryDto.UpdateEntry updateRequestEntry) {

        if( updateRequestEntry.getEntryName() != null ){
            existingEntry.setEntryName(updateRequestEntry.getEntryName());
        }

        if( updateRequestEntry.getDescription() != null ){
            existingEntry.setDescription(updateRequestEntry.getDescription());
        }

        if( isEntryMediaUpdated(updateRequestEntry.getMediaUrl())){
            existingEntry.setMediaType(updateRequestEntry.getMediaType());
            existingEntry.setMediaUrl(updateRequestEntry.getMediaUrl());
        }

        if(isThumbnailUpdated(updateRequestEntry.getThumbnail())){
            existingEntry.setThumbnail(updateRequestEntry.getThumbnail());
        }
    }


    private boolean isEntryMediaUpdated(String mediaUrl){
        return mediaUrl != null && !mediaUrl.isEmpty();
    }

    private boolean isThumbnailUpdated(String thumbnail){
        return thumbnail != null && !thumbnail.isEmpty();
    }
}
