package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.member.Member;

public interface ApplicantRepositoryCustom {

    Applicant findApplicantByMemberAndRecruiting(Member member, Recruiting recruiting);

}