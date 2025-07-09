package com.buck.vsplay.domain.vstopic.service.finder;

import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicFinder {
    private final VsTopicRepository vsTopicRepository;

    public VsTopic findExistingById(Long topicId){
        return vsTopicRepository.findByIdAndDeletedFalse(topicId).orElseThrow(
                () -> new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));
    }

    public void validateTopicExists(Long topicId){
        if(!vsTopicRepository.existsByIdAndDeletedFalse(topicId)) {
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND);
        }
    }
}
