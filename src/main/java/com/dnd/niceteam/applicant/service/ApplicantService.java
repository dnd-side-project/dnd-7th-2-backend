package com.dnd.niceteam.applicant.service;

import com.dnd.niceteam.applicant.dto.ApplicantCreation;
import com.dnd.niceteam.domain.code.ProgressStatus;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.recruiting.Applicant;
import com.dnd.niceteam.domain.recruiting.ApplicantRepository;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.domain.recruiting.exception.ApplicantNotFoundException;
import com.dnd.niceteam.domain.recruiting.exception.ApplyCancelImpossibleRecruitingException;
import com.dnd.niceteam.domain.recruiting.exception.ApplyImpossibleRecruitingException;
import com.dnd.niceteam.domain.recruiting.exception.RecruitingNotFoundException;
import com.dnd.niceteam.member.util.MemberUtils;
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
    public ApplicantCreation.ResponseDto addApplicant(Long recruitingId, String username) {
        Member member = MemberUtils.findMemberByEmail(username, memberRepository);
        Recruiting recruiting = getRecruitingtEntity(recruitingId);

        if (isApplyImpossible(recruiting.getStatus())) {
            throw new ApplyImpossibleRecruitingException("Recruiting Status = " + recruiting.getStatus());
        }

        Applicant savedApplicant = applicantRepository.save(Applicant.builder()
                .member(member)
                .recruiting(recruiting)
                .build());
        ApplicantCreation.ResponseDto responseDto = new ApplicantCreation.ResponseDto();
        responseDto.setId(savedApplicant.getId());
        return responseDto;
    }

    private boolean isApplyImpossible(ProgressStatus recruitingStatus) {
        return !ProgressStatus.IN_PROGRESS.equals(recruitingStatus);
    }

    private Recruiting getRecruitingtEntity(Long recruitingId) {
        return recruitingRepository.findById(recruitingId)
                .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + recruitingId));
    }

    @Transactional
    public void removeApplicant(Long recruitingId, String username) {
        Member member = MemberUtils.findMemberByEmail(username, memberRepository);
        Applicant foundApplicant = applicantRepository.findByMemberIdAndRecruitingId(member.getId(), recruitingId)
                .orElseThrow(() -> new ApplicantNotFoundException("memberId = " + member.getId() + ", recruitingId = " + recruitingId));

        if (alreadyJoined(foundApplicant)) {
            throw new ApplyCancelImpossibleRecruitingException(foundApplicant.getId() + "의 join 상태 : " + foundApplicant.getJoined());
        }
        applicantRepository.delete(foundApplicant);
    }

    private boolean alreadyJoined(Applicant applicant) {
        return applicant.getJoined().equals(Boolean.TRUE);
    }
}
