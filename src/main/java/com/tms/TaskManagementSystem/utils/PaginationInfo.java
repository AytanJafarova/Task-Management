package com.tms.TaskManagementSystem.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationInfo {
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalCount;
    private Boolean hasPreviousPage;
    private Boolean hasNextPage;
}
