package com.buck.vsplay.domain.statistics.specification;

import com.buck.vsplay.domain.statistics.entity.EntryStatistics;
import jakarta.persistence.criteria.Order;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryStatsSpecification {

    public static Specification<EntryStatistics> idFilter(Long id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("topicEntry").get("topic").get("id"), id);
    }


    public static Specification<EntryStatistics> orderFilter(boolean totalMatchesDesc, boolean totalWinsDesc, boolean winRateDesc){
        return (root, query, criteriaBuilder) ->{
            if( query != null){
                List<Order> orders = new ArrayList<>();
                orders.add(totalMatchesDesc
                        ? criteriaBuilder.desc(root.get("totalMatches"))
                        : criteriaBuilder.asc(root.get("totalMatches")));

                orders.add(totalWinsDesc
                        ? criteriaBuilder.desc(root.get("totalWins"))
                        : criteriaBuilder.asc(root.get("totalWins")));

                orders.add(winRateDesc
                        ? criteriaBuilder.desc(root.get("winRate"))
                        : criteriaBuilder.asc(root.get("winRate")));

                query.orderBy(orders);
            }
            return criteriaBuilder.conjunction();
        };
    }
}
