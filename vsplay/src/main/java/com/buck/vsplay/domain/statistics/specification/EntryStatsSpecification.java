package com.buck.vsplay.domain.statistics.specification;

import com.buck.vsplay.domain.statistics.entity.EntryStatistics;
import com.buck.vsplay.global.constants.OrderType;
import jakarta.persistence.criteria.Order;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryStatsSpecification {

    public static Specification<EntryStatistics> idFilter(Long id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("topicEntry").get("topic").get("id"), id);
    }

    public static Specification<EntryStatistics> entryNameFilter(String keyword) {
        return (root, query, criteriaBuilder) -> {
                if(keyword != null && !keyword.isEmpty()) {
                    criteriaBuilder.like(root.get("topicEntry").get("entryName"), "%" + keyword + "%");
                }
                return null;
        };
    }


    public static Specification<EntryStatistics> orderFilter(
            OrderType rankOrderType,
            OrderType totalMatchesOrderType,
            OrderType totalWinsOrderType,
            OrderType winRateOrderType){
        return (root, query, criteriaBuilder) ->{
            if( query != null){
                List<Order> orders = new ArrayList<>();

                Order rankOrder = rankOrderType.convertOrderTypeToJpaOrder(criteriaBuilder, root.get("rank"));
                Order totalMatchesOrder = totalMatchesOrderType.convertOrderTypeToJpaOrder(criteriaBuilder, root.get("totalMatches"));
                Order totalWinsOrder = totalWinsOrderType.convertOrderTypeToJpaOrder(criteriaBuilder, root.get("totalWins"));
                Order winRateOrder = winRateOrderType.convertOrderTypeToJpaOrder(criteriaBuilder, root.get("winRate"));

                Optional.ofNullable(rankOrder).ifPresent(orders::add);
                Optional.ofNullable(totalMatchesOrder).ifPresent(orders::add);
                Optional.ofNullable(totalWinsOrder).ifPresent(orders::add);
                Optional.ofNullable(winRateOrder).ifPresent(orders::add);

                query.orderBy(orders);
            }
            return criteriaBuilder.conjunction();
        };
    }
}
