package com.dnd.niceteam.domain.bookmark.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Getter
public class LectureBookmarkDto extends BookmarkDto {

    private String lectureName;

    @Setter
    private Collection<LectureTimeDto> lectureTimes = Collections.emptyList();

    @QueryProjection
    public LectureBookmarkDto(
            Long id,
            Long recruitingId,
            Long projectId,
            String title,
            LocalDate recruitingEndDate,
            Integer commentCount,
            Integer bookmarkCount,
            Integer recruitingMemberCount,
            String lectureName) {
        super(
                id,
                recruitingId,
                projectId,
                title,
                recruitingEndDate,
                commentCount,
                bookmarkCount,
                recruitingMemberCount
        );
        this.lectureName = lectureName;
    }
}
