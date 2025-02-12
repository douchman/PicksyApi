package com.buck.vsplay.global.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pagination {
    private Integer totalPages;
    private Long totalItems;
    private Integer currentPage;
    private Integer pageSize;
}
