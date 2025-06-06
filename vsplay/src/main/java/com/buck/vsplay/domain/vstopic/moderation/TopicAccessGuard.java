package com.buck.vsplay.domain.vstopic.moderation;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.global.constants.ModerationStatus;
import com.buck.vsplay.global.constants.Visibility;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicAccessGuard {
    public static void validateTopicAccess(VsTopic topic, Member member){

        if(topic == null){
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND);
        }

        boolean isNotPublic = !isPublicTopic(topic.getVisibility());
        boolean isNotOwner = member == null || !topic.getMember().getId().equals(member.getId());
        boolean isNotPassed = topic.getModerationStatus() != ModerationStatus.PASSED;

        if( isNotOwner && isNotPublic) {
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_CREATOR_ONLY);
        }

        if(isNotOwner && isNotPassed){
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_PUBLIC);
        }
    }

    private static boolean isPublicTopic(Visibility visibility){
        return visibility == Visibility.PUBLIC;
    }
}
