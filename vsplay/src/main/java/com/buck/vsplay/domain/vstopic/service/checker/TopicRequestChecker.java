package com.buck.vsplay.domain.vstopic.service.checker;

import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.service.support.FilterTextListBuilder;
import com.buck.vsplay.global.constants.Visibility;
import com.buck.vsplay.global.util.gpt.client.BadWordFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TopicRequestChecker {

    private final BadWordFilter badWordFilter;

    public void checkTopicCreateRequest(VsTopicDto.VsTopicCreateRequest request) {
        validationModerationContent(request.getVisibility(), request.getAccessCode());
        filterTopicContentTexts(request.getTitle(), request.getSubject(), request.getDescription());
    }

    public void checkTopicUpdateRequest(VsTopicDto.VsTopicUpdateRequest request) {
        validationModerationContent(request.getVisibility(), request.getAccessCode());
        filterTopicContentTexts(request.getTitle(), request.getSubject(), request.getDescription());
    }

    private void filterTopicContentTexts(String title, String subject, String description){
        List<String> stringList = FilterTextListBuilder.buildStringList(
                title,
                subject,
                description);
        boolean hasBadWord = badWordFilter.containsBadWords(stringList);

        if( hasBadWord ){
            throw new VsTopicException(VsTopicExceptionCode.BAD_WORD_DETECTED);
        }
    }

    private void validationModerationContent(Visibility visibility, String accessCode){
        if(isMissingPasswordForProtectedTopic(visibility, accessCode)){
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_PASSWORD_REQUIRE);
        }
    }

    private boolean isMissingPasswordForProtectedTopic(Visibility visibility, String accessCode){
        return visibility == Visibility.PASSWORD && (accessCode == null || accessCode.isBlank());
    }
}
