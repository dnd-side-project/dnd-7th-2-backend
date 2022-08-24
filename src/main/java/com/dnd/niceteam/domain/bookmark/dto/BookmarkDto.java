package com.dnd.niceteam.domain.bookmark.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class BookmarkDto {

    private Long id;

    private Long recruitingId;

    private Long projectId;

    private String title;

    private LocalDate recruitingEndDate;

    private Integer commentCount;

    private Integer bookmarkCount;

    private Integer recruitingMemberCount;
}
