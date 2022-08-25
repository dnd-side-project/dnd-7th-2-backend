package com.dnd.niceteam.domain.comment;

import com.dnd.niceteam.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    Page<Comment> findPageByRecruitingIdOrderByGroupNoAscCreatedDateAsc(Pageable pageable, Long recruitingId);

    Page<Comment> findPageByMemberOrderByCreatedDateDesc(Pageable pageable, Member member);
}
