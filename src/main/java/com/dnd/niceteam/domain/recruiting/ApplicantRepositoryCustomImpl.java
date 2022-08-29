package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.member.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.dnd.niceteam.domain.recruiting.QApplicant.applicant;

@RequiredArgsConstructor
public class ApplicantRepositoryCustomImpl implements ApplicantRepositoryCustom{
    private final JPAQueryFactory query;

    @Override
    public PageImpl<Applicant> findAllByMemberAndRecruitingStatusAndJoinedOrderByCreatedDateDesc(Member member,
                                                                                 RecruitingStatus recruitingStatus,
                                                                                 Boolean applicantJoined,
                                                                                 Pageable pageable) {
        List<Applicant> applicants = query
                .selectFrom(applicant)
                .where(
                        applicant.member.eq(member),
                        recruitingStatueEq(applicant.recruiting.status, recruitingStatus),
                        joinedEq(applicant.joined, applicantJoined)
                )
                .orderBy(applicant.recruiting.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = query.select(applicant.count())
                .from(applicant)
                .where(
                        applicant.member.eq(member),
                        recruitingStatueEq(applicant.recruiting.status, recruitingStatus),
                        joinedEq(applicant.joined, applicantJoined)
                )
                .fetchOne();
        return new PageImpl<>(applicants, pageable, totalCount);
    }

    private BooleanBuilder joinedEq(BooleanPath joined, Boolean applicantJoined) {
        return applicantJoined != null ? new BooleanBuilder(joined.eq(applicantJoined)) : new BooleanBuilder();
    }

    private BooleanBuilder recruitingStatueEq(EnumPath<RecruitingStatus> status, RecruitingStatus recruitingStatus) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (recruitingStatus == null)
            return booleanBuilder;

        if (recruitingStatus.equals(RecruitingStatus.IN_PROGRESS))
            booleanBuilder = new BooleanBuilder(status.eq(recruitingStatus));
        else if (recruitingStatus.equals(RecruitingStatus.DONE) || recruitingStatus.equals(RecruitingStatus.FAILED))
            booleanBuilder = new BooleanBuilder(status.eq(RecruitingStatus.DONE).or(status.eq(RecruitingStatus.FAILED)));

        return booleanBuilder;
    }
}