package com.tms.TaskManagementSystem.utils;

import org.springframework.data.domain.Page;

public class PaginationUtil {
    public static <T> PaginationInfo getPaginationInfo(Page<T> page)
    {
        return PaginationInfo.builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .hasPreviousPage(page.hasPrevious())
                .hasNextPage(page.hasNext())
                .build();
    }
}
