package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitingRepository extends JpaRepository<Recruiting, Long>, RecruitingRepositoryCustom {

    Page<Recruiting> findPageByMemberOrderByCreatedDateDesc(Pageable pageable, Member member);
    Page<Recruiting> findPageByMemberAndStatusOrderByCreatedDateDesc(Pageable pageable, Member member, RecruitingStatus status);
}
