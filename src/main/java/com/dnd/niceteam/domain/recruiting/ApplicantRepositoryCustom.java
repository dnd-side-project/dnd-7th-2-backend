package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicantRepositoryCustom {
    Page<Applicant> findAllByMemberAndRecruitingStatusAndJoinedOrderByCreatedDateDesc(Member member, RecruitingStatus recruitingStatus,
                                                                                      Boolean applicantJoined, Pageable pageable);
}