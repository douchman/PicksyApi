package com.buck.vsplay.domain.vstopic.service.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterTextListBuilder {

    public static List<String> buildStringList(String ... strings){
        if(strings == null || strings.length == 0){
            return Collections.emptyList();
        }
        return Arrays.stream(strings)
                .filter(Objects::nonNull)
                .toList();
    }
}
