package com.dnd.niceteam.common.util;

import com.dnd.niceteam.common.dto.Pagination;
import org.springframework.data.domain.Page;

public interface PaginationUtil {

    static <T> Pagination<T> pageToPagination(Page<T> page) {
        return Pagination.<T>builder()
                .page(page.getPageable().getPageNumber())
                .perSize(page.getPageable().getPageSize())
                .totalCount((int) page.getTotalElements())
                .contents(page.getContent())
                .build();
    }
}
