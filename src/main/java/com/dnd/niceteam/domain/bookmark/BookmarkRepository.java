package com.dnd.niceteam.domain.bookmark;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryCustom {

    boolean existsByMemberAndRecruiting(Member member, Recruiting recruiting);
}
