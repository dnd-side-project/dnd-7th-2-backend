package com.dnd.niceteam.domain.bookmark;

import com.dnd.niceteam.domain.bookmark.dto.LectureBookmarkDto;
import com.dnd.niceteam.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkRepositoryCustom {

    boolean existsByIdAndEmail(long bookmarkId, String email);

    Page<LectureBookmarkDto> findLectureBookmarkDtoPageByMember(Pageable pageable, Member member);
}
