package com.dnd.niceteam.domain.bookmark.dto;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SideBookmarkDto extends BookmarkDto {

    private Field field;

    private FieldCategory fieldCategory;

    @QueryProjection
    public SideBookmarkDto(
            Long id,
            Long recruitingId,
            Long projectId,
            String title,
            LocalDate recruitingEndDate,
            Integer commentCount,
            Integer bookmarkCount,
            Integer recruitingMemberCount,
            Field field,
            FieldCategory fieldCategory) {
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

        this.field = field;
        this.fieldCategory = fieldCategory;
    }
}
