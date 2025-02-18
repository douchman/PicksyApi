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

    // 삭제 여부 필터
    public static Specification<TopicComment> deleteFilter(boolean delete){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deleted"), delete));
    }

    // 모든 필터 적용
    public static Specification<TopicComment> withAllFilters(String keyword, boolean delete){
        return Specification.where(keywordFilter(keyword)).and(deleteFilter(delete));
    }
}
