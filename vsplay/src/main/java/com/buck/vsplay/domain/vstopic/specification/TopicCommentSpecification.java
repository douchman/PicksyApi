package com.buck.vsplay.domain.vstopic.specification;

import com.buck.vsplay.domain.vstopic.entity.TopicComment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicCommentSpecification {

    // 키워드 검색
    public static Specification<TopicComment> keywordFilter(String keyword){
        return (root, query, criteriaBuilder) -> {
            if(keyword != null && !keyword.isEmpty()) {
                return criteriaBuilder.like(root.get("content"), "%" + keyword + "%");
            }
            return null;
        };
    }
}
