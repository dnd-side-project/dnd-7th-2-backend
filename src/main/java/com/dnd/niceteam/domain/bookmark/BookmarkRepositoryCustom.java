package com.dnd.niceteam.domain.bookmark;

public interface BookmarkRepositoryCustom {

    boolean existsByIdAndEmail(long bookmarkId, String email);
}
