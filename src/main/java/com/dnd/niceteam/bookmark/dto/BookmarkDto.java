package com.dnd.niceteam.bookmark.dto;

import com.dnd.niceteam.domain.bookmark.Bookmark;
import lombok.Data;

@Data
public class BookmarkDto {

    private Long id;

    private Long recruitingId;

    private String recruitingTitle;

    public static BookmarkDto of(Bookmark bookmark) {
        BookmarkDto bookmarkDto = new BookmarkDto();
        bookmarkDto.setId(bookmark.getId());
        bookmarkDto.setRecruitingId(bookmark.getRecruiting().getId());
        bookmarkDto.setRecruitingTitle(bookmark.getRecruiting().getTitle());
        return bookmarkDto;
    }
}
