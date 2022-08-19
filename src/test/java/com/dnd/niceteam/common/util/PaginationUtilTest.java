package com.dnd.niceteam.common.util;

import com.dnd.niceteam.common.dto.Pagination;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class PaginationUtilTest {

    @Test
    void pageToPagination() {
        // given
        Page<Integer> page = new PageImpl<>(IntStream.range(0, 8).boxed().collect(Collectors.toList()),
                PageRequest.of(0, 10), 8);

        // when
        Pagination<Integer> pagination = PaginationUtil.pageToPagination(page);

        // then
        assertThat(pagination.getPage()).isZero();
        assertThat(pagination.getPerSize()).isEqualTo(10);
        assertThat(pagination.getTotalCount()).isEqualTo(8);
        assertThat(pagination.getContents())
                .containsExactly(0, 1, 2, 3, 4, 5, 6, 7);
    }
}