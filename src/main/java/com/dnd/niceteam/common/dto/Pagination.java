package com.dnd.niceteam.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
public class Pagination<T> {

    private final int page;

    private final int perSize;

    private final int totalCount;

    private final int totalPages;

    private final boolean prev;

    private final boolean next;

    private final List<T> contents;

    @Builder
    private Pagination(int page, int perSize, int totalCount, List<T> contents) {
        this.page = page;
        this.perSize = perSize;
        this.totalCount = totalCount;

        this.contents = Objects.requireNonNullElseGet(contents, ArrayList::new);

        // 연산된 값
        int pageWithFullContent = totalCount / perSize;
        int pageWithLackContent = totalCount % perSize == 0 ? 0 : 1;
        this.totalPages = pageWithFullContent + pageWithLackContent;

        this.prev = page > 1;
        this.next = page < totalPages;
    }

}
