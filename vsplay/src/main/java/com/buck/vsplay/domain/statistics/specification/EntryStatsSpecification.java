package com.buck.vsplay.domain.statistics.specification;

import com.buck.vsplay.domain.statistics.entity.EntryStatistics;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryStatsSpecification {

    public static Specification<EntryStatistics> idFilter(Long id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("topicEntry").get("topic").get("id"), id);
    }

    public static Specification<EntryStatistics> entryNameFilter(String keyword) {
        return (root, query, criteriaBuilder) -> {
                if(keyword != null && !keyword.isEmpty()) {
                    return criteriaBuilder.like(root.get("topicEntry").get("entryName"), "%" + keyword + "%");
                }
                return criteriaBuilder.conjunction();
        };
    }
}
