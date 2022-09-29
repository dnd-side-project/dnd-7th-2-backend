package com.dnd.niceteam.applicant.service;

import com.dnd.niceteam.applicant.dto.ApplicantCreation;
import com.dnd.niceteam.applicant.dto.ApplicantFind;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.common.util.PaginationUtil;
import com.dnd.niceteam.domain.recruiting.*;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.project.ProjectMember;
import com.dnd.niceteam.domain.project.ProjectMemberRepository;
import com.dnd.niceteam.domain.recruiting.exception.*;
import com.dnd.niceteam.member.util.MemberUtils;
import com.dnd.niceteam.project.exception.ProjectMemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final RecruitingRepository recruitingRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;

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

    @Transactional
    public void removeApplicant(Long recruitingId, String username) {
        Member member = MemberUtils.findMemberByEmail(username, memberRepository);
        Recruiting recruiting = recruitingRepository.findById(recruitingId)
                .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + recruitingId));
        Applicant foundApplicant = applicantRepository.findByMemberIdAndRecruitingId(member.getId(), recruitingId)
                .orElseThrow(() -> new ApplicantNotFoundException("memberId = " + member.getId() + ", recruitingId = " + recruitingId));

        if (alreadyRecruitingClosed(recruiting)) {
            throw new ApplyCancelImpossibleRecruitingException("recruiting status : " + recruiting.getStatus());
        }

        if (alreadyJoined(foundApplicant)) {
            ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(recruiting.getProject(), member)
                            .orElseThrow(() -> new ProjectMemberNotFoundException(recruiting.getProject().getId(), member.getId()));
            projectMemberRepository.delete(projectMember);
        }
        applicantRepository.delete(foundApplicant);
    }

    public Pagination<ApplicantFind.ListResponseDto> getMyApplicnts(int page, int perSize,
                                                                    RecruitingStatus recruitingStatus,
                                                                    Boolean applicantJoined, String username) {
        if (isInvalidFiltering(recruitingStatus, applicantJoined)) {
            throw new InvalidApplicantTypeException(recruitingStatus, applicantJoined);
        }
        Member member = MemberUtils.findMemberByEmail(username, memberRepository);

        Pageable pageable = PageRequest.of(page - 1, perSize);
        Page<ApplicantFind.ListResponseDto> applicationPages = applicantRepository.findAllByMemberAndRecruitingStatusAndJoinedOrderByCreatedDateDesc(
                member, recruitingStatus, applicantJoined, pageable)
                .map(ApplicantFind.ListResponseDto::from);

        return PaginationUtil.pageToPagination(applicationPages);
    }

    private boolean isInvalidFiltering(RecruitingStatus recruitingStatus, Boolean applicantJoined) {
        return (isNull(applicantJoined) && !isNull(recruitingStatus))
                || (isNull(recruitingStatus) && !isNull(applicantJoined));
    }

    private boolean isApplyImpossible(RecruitingStatus recruitingStatus) {
        return !RecruitingStatus.IN_PROGRESS.equals(recruitingStatus);
    }

    private Recruiting getRecruitingtEntity(Long recruitingId) {
        return recruitingRepository.findById(recruitingId)
                .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + recruitingId));
    }

    private boolean alreadyRecruitingClosed(Recruiting recruiting) {
        return recruiting.getStatus() != RecruitingStatus.IN_PROGRESS;
    }

    private boolean alreadyJoined(Applicant applicant) {
        return applicant.getJoined().equals(Boolean.TRUE);
    }
}
