package com.buck.vsplay.global.util;

import com.buck.vsplay.global.constants.OrderType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SortUtil {

    public static <T extends Enum<T>> Sort buildSort(Map<T, OrderType> sortMap, Function<T, String> propertyExtractor) {
        List<Sort.Order> orders = sortMap.entrySet().stream()
                .map(e -> e.getValue().toSortOrder(propertyExtractor.apply(e.getKey())))
                .flatMap(Optional::stream)
                .toList();

        return Sort.by(orders);
    }
}
