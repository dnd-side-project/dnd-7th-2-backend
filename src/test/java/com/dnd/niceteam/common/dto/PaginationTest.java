package com.dnd.niceteam.common.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaginationTest {

    @DisplayName("페이지네이션 DTO 생성 테스트")
    @Test
    void testPaginationDTO() {
        // given
        int page = 12;
        int perSize = 5;
        int totalCount = 113;

        int totalPages = totalCount / perSize + (totalCount % perSize == 0 ? 1 : 0);

        String content1 = "테스트 내용 1";
        String content2 = "테스트 내용 2";
        List<String> contents = List.of(content1, content2);

        // when
        Pagination pagination = Pagination.<String>builder()
                .page(page)
                .perSize(perSize)
                .totalCount(totalCount)
                .contents(contents)
                .build();

        // then
        assertAll(
                () -> assertEquals(pagination.getPage(), page),
                () -> assertEquals(pagination.getPerSize(), perSize),
                () -> assertEquals(pagination.getTotalCount(), totalCount),
                () -> assertEquals(pagination.getTotalPages(), totalPages),
                () -> assertEquals(pagination.getContents().size(), contents.size()),
                () -> assertEquals(pagination.isPrev(), page > 1),
                () -> assertEquals(pagination.isNext(), page < pagination.getTotalPages())
        );

    }

}