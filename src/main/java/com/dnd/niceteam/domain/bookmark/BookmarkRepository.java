package com.dnd.niceteam.domain.bookmark;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryCustom {

    boolean existsByMemberAndRecruiting(Member member, Recruiting recruiting);

    Page<Bookmark> findPageByMember(Pageable pageable, Member member);

    @EntityGraph(attributePaths = "recruiting")
    Page<Bookmark> findPageWithRecruitingByMember(Pageable pageable, Member member);
}
