package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.dnd.niceteam.domain.recruiting.QApplicant.applicant;

@RequiredArgsConstructor
public class ApplicantRepositoryCustomImpl implements ApplicantRepositoryCustom{
    private final JPAQueryFactory query;

    @Override
    public Applicant findApplicantByMemberAndRecruiting(Member member, Recruiting recruiting) {
        return query
                .selectFrom(applicant)
                .where(
                        applicant.member.eq(member),
                        applicant.recruiting.eq(recruiting)
                )
                .fetchOne();
    }
}
