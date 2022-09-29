package com.dnd.niceteam.applicant.service;

import com.dnd.niceteam.applicant.dto.ApplicantCreation;

import com.dnd.niceteam.applicant.dto.ApplicantFind;
import com.dnd.niceteam.comment.EntityFactoryForTest;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.recruiting.RecruitingStatus;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.recruiting.Applicant;
import com.dnd.niceteam.domain.recruiting.ApplicantRepository;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.domain.recruiting.exception.ApplicantNotFoundException;
import com.dnd.niceteam.domain.recruiting.exception.ApplyCancelImpossibleRecruitingException;
import com.dnd.niceteam.domain.recruiting.exception.ApplyImpossibleRecruitingException;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.error.exception.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicantServiceTest {
    @InjectMocks
    private ApplicantService applicantService;

    @Mock
    private ApplicantRepository mockApplicantRepository;

    @Mock
    private MemberRepository mockMemberRepository;

    @Mock
    private RecruitingRepository mockRecruitingRepository;

    private final String email = "tester@gmail.com";
    private final Long applicantId = 1L;
    private final Long memberId = 1L;
    private final Long recruitingId = 1L;
    private final Long universityId = 1L;

    Member member;
    Recruiting recruiting;
    Applicant applicant;

    @DisplayName("모집글 지원 등록 서비스 테스트")
    @Test
    void create() {
        // given
        member = Member.builder()
                .id(memberId)
                .account(Account.builder().email(email).build())
                .build();
        given(mockMemberRepository.findByEmail(email))
                .willReturn(Optional.of(member));

        recruiting = Recruiting.builder()
                .id(recruitingId)
                .status(RecruitingStatus.IN_PROGRESS)
                .build();
        given(mockRecruitingRepository.findById(recruitingId))
                .willReturn(Optional.of(recruiting));

        applicant = Applicant.builder()
                .id(applicantId)
                .member(member)
                .recruiting(recruiting)
                .joined(Boolean.FALSE)
                .build();
        given(mockApplicantRepository.save(any(Applicant.class))).willReturn(applicant);

        // when
        ApplicantCreation.ResponseDto responseDto = applicantService.addApplicant(recruitingId, email);

        // then
        verify(mockApplicantRepository).save(any(Applicant.class));
        assertThat(responseDto.getId()).isEqualTo(applicantId);
    }

    @Transactional
    @DisplayName("예외 - 모집글 지원 불가능 상태(DONE)")
    @Test
    void impossibleApplyException() {
        // given
        member = Member.builder()
                .id(memberId)
                .account(Account.builder().email(email).build())
                .build();
        given(mockMemberRepository.findByEmail(email))
                .willReturn(Optional.of(member));

        recruiting = Recruiting.builder()
                .id(recruitingId)
                .status(RecruitingStatus.DONE)
                .build();
        given(mockRecruitingRepository.findById(recruitingId))
                .willReturn(Optional.of(recruiting));

        // when
        RuntimeException e = Assertions.assertThrows(ApplyImpossibleRecruitingException.class
                , () -> applicantService.addApplicant(recruitingId, email));
        // then
        assertThat(e.getMessage()).startsWith(ErrorCode.APPLY_IMPOSSIBLE_RECRUITING.name());
    }


    @Transactional
    @DisplayName("모집글 지원 취소")
    @Test
    void remove() {
        // given
        member = Member.builder()
                .id(memberId)
                .account(Account.builder().email(email).build())
                .build();
        given(mockMemberRepository.findByEmail(email))
                .willReturn(Optional.of(member));

        recruiting = Recruiting.builder()
                .id(recruitingId)
                .status(RecruitingStatus.IN_PROGRESS)
                .build();
        given(mockRecruitingRepository.findById(recruitingId))
                .willReturn(Optional.of(recruiting));

        applicant = Applicant.builder()
                .id(applicantId)
                .recruiting(recruiting)
                .member(member)
                .joined(Boolean.FALSE)
                .build();
        given(mockApplicantRepository.findByMemberIdAndRecruitingId(memberId, recruitingId))
                .willReturn(Optional.of(applicant));

        // when
        applicantService.removeApplicant(recruitingId, email);

        // then
        RuntimeException e = Assertions.assertThrows(ApplicantNotFoundException.class
                , () -> mockApplicantRepository.findById(applicantId)
                        .orElseThrow(() -> new ApplicantNotFoundException("applicantId= " + applicantId)));
        assertThat(e.getMessage()).startsWith(ErrorCode.APPLICANT_NOT_FOUND.name());
    }

    @Transactional
    @DisplayName("예외 - 모집글 지원 취소 불가능 상태(DONE)")
    @Test
    void impossibleApplyCancelException() {
        // given
        member = Member.builder()
                .id(memberId)
                .account(Account.builder().email(email).build())
                .build();
        given(mockMemberRepository.findByEmail(email))
                .willReturn(Optional.of(member));

        recruiting = EntityFactoryForTest.createRecruiting(member,
                EntityFactoryForTest.createLectureProject(EntityFactoryForTest.createDepartment(
                        University.builder()
                                .id(universityId)
                                .emailDomain("email-domain.ac.kr")
                                .name("univ")
                                .build()
                )), Type.LECTURE);
        recruiting.updateStatus(RecruitingStatus.DONE);
        given(mockRecruitingRepository.findById(recruitingId))
                .willReturn(Optional.of(recruiting));

        applicant = Applicant.builder()
                .id(applicantId)
                .recruiting(recruiting)
                .member(member)
                .joined(Boolean.TRUE)
                .build();
        given(mockApplicantRepository.findByMemberIdAndRecruitingId(memberId, recruitingId))
                .willReturn(Optional.of(applicant));

        // when
        RuntimeException e = Assertions.assertThrows(ApplyCancelImpossibleRecruitingException.class
                , () -> applicantService.removeApplicant(recruitingId, email));
        // then
        assertThat(e.getMessage()).startsWith(ErrorCode.APPLY_CANCEL_IMPOSSIBLE_RECRUITING.name());
    }

    @Transactional
    @DisplayName("모집글 지원 현황 목록 조회(필터링)")
    @Test
    void getMyApplication() {
        // given
        Member mockMember = mock(Member.class, RETURNS_DEEP_STUBS);
        Recruiting mockRecruiting = mock(Recruiting.class, RETURNS_DEEP_STUBS);
        Applicant applicant = Applicant.builder()
                .member(mockMember)
                .recruiting(mockRecruiting)
                .joined(Boolean.TRUE)
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        when(mockMemberRepository.findByEmail(email)).thenReturn(Optional.ofNullable(mockMember));
        when(mockApplicantRepository.findAllByMemberAndRecruitingStatusAndJoinedOrderByCreatedDateDesc(
                any(Member.class), any(RecruitingStatus.class), anyBoolean(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(applicant), pageable, 1L));
        // when
        Pagination<ApplicantFind.ListResponseDto> myApplicntsDto = applicantService.getMyApplicnts(1, 10, RecruitingStatus.IN_PROGRESS, Boolean.TRUE, email);

        // then
        assertThat(myApplicntsDto.getContents().size()).isEqualTo(1);
        assertThat(myApplicntsDto.getTotalCount()).isEqualTo(1);
    }
}