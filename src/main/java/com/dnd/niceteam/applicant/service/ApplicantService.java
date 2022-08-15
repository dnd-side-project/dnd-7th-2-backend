package com.dnd.niceteam.applicant.service;

import com.dnd.niceteam.applicant.dto.ApplicantCreation;
import com.dnd.niceteam.domain.code.ProgressStatus;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.member.exception.MemberNotFoundException;
import com.dnd.niceteam.domain.recruiting.Applicant;
import com.dnd.niceteam.domain.recruiting.ApplicantRepository;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.recruiting.exception.ApplyImpossibleRecruitingStatusException;
import com.dnd.niceteam.recruiting.exception.RecruitingNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final RecruitingRepository recruitingRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ApplicantCreation.ResponseDto addApplicant(Long recruitingId, Long memberId) {
        Member member = getMemberEntity(memberId);
        Recruiting recruiting = getRecruitingtEntity(recruitingId);

        if (isApplyImpossible(recruiting.getStatus())) {
            throw new ApplyImpossibleRecruitingStatusException("Recruiting Status = " + recruiting.getStatus());
        }

        Applicant savedApplicant = applicantRepository.save(Applicant.builder()
                .member(member)
                .recruiting(recruiting)
                .build());
        return new ApplicantCreation.ResponseDto(savedApplicant.getId());
    }

    private boolean isApplyImpossible(ProgressStatus recruitingStatus) {
        return !ProgressStatus.IN_PROGRESS.equals(recruitingStatus);
    }

    private Recruiting getRecruitingtEntity(Long recruitingId) {
        return recruitingRepository.findById(recruitingId)
                .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + recruitingId));
    }

    private Member getMemberEntity(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("memberId = " + memberId));
    }
}
