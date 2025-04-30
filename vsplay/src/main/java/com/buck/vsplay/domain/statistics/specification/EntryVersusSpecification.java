package com.buck.vsplay.domain.statistics.specification;

import com.buck.vsplay.domain.statistics.entity.EntryVersusStatistics;
import jakarta.persistence.criteria.Order;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryVersusSpecification {

    public static Specification<EntryVersusStatistics> entryIdFilter(Long entryId){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("topicEntry").get("id"), entryId);
    }

    public static Specification<EntryVersusStatistics> orderFilter(){
        return (root, query, criteriaBuilder) -> {
            if( query != null) {
                List<Order> orders = new ArrayList<>();
                Order winsOrder = criteriaBuilder.desc(root.get("wins")); // 승리 횟수 order -> DESC
                Order winRateOrder = criteriaBuilder.desc(root.get("winRate")); // 승률 order -> DESC

                Optional.ofNullable(winsOrder).ifPresent(orders::add);
                Optional.ofNullable(winRateOrder).ifPresent(orders::add);

                query.orderBy(orders);

            }
            return criteriaBuilder.conjunction();
        };
    }

}
