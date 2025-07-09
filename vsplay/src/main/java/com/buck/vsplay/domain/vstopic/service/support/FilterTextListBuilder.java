package com.buck.vsplay.domain.vstopic.service.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterTextListBuilder {

    public static List<String> buildStringList(String ... strings){
        return List.of(strings);
    }
}
