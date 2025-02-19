package com.buck.vsplay.domain.vstopic.specification;

import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VsTopicSpecification {

    // 회원 식별자 검색
    public static Specification<VsTopic> memberIdFilter(Long memberId){
        return (root, query, criteriaBuilder) ->
          memberId != null ? criteriaBuilder.equal(root.get("member").get("id") , memberId) : null;
    }

    // 키워드 검색
    public static Specification<VsTopic> keywordFilter(String keyword){
        return (root, query, criteriaBuilder) -> {
            if(keyword != null && !keyword.isEmpty()) {
                Predicate titlePredicate = criteriaBuilder.like(root.get("title"), "%" + keyword + "%");
                Predicate subjectPredicate = criteriaBuilder.like(root.get("subject"), "%" + keyword + "%");
                return criteriaBuilder.or(titlePredicate, subjectPredicate);
            }
            return null;
        };
    }

    // 삭제 여부 필터
    public static Specification<VsTopic> deleteFilter(boolean delete){
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("deleted"), delete);
    }

    public static Specification<VsTopic> withAllFilters(Long memberId, String keyword, boolean delete) {
        return Specification.where(memberIdFilter(memberId).and(keywordFilter(keyword))).and(deleteFilter(delete));
    }
}
