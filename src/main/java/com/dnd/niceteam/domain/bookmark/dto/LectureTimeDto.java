package com.dnd.niceteam.domain.bookmark.dto;

import com.dnd.niceteam.domain.code.DayOfWeek;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@ToString
public class LectureTimeDto {

    private Long projectId;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    @QueryProjection
    public LectureTimeDto(Long projectId, DayOfWeek dayOfWeek, LocalTime startTime) {
        this.projectId = projectId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
    }
}
