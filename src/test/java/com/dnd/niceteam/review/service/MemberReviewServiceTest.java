package com.dnd.niceteam.review.service;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.memberscore.MemberScore;
import com.dnd.niceteam.domain.memberscore.MemberScoreRepository;
import com.dnd.niceteam.domain.project.*;
import com.dnd.niceteam.domain.review.MemberReview;
import com.dnd.niceteam.domain.review.MemberReviewRepository;
import com.dnd.niceteam.review.MemberReviewTestFactory;
import com.dnd.niceteam.review.dto.MemberReviewRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberReviewServiceTest {

    @InjectMocks
    MemberReviewService memberReviewService;

    @Mock
    MemberReviewRepository memberReviewRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    MemberScoreRepository memberScoreRepository;

    static final long reviwerId = 1L;
    static final long revieweeId = 2L;
    static final String email = "tester@gmail.com";

    User currentUser;
    Member reviewerMember;
    Member revieweeMember;
    ProjectMember reviewer;

    ProjectMember reviewee;

    @BeforeEach
    void setUp() {
        currentUser = mock(User.class);
        when(currentUser.getUsername()).thenReturn(email);

        reviewerMember = mock(Member.class, RETURNS_DEEP_STUBS);
        when(reviewerMember.getId()).thenReturn(reviwerId);

        revieweeMember = mock(Member.class, RETURNS_DEEP_STUBS);
        when(revieweeMember.getId()).thenReturn(revieweeId);

        reviewer = mock(ProjectMember.class, RETURNS_DEEP_STUBS);
        when(reviewer.getMember().getId()).thenReturn(reviwerId);

        reviewee = mock(ProjectMember.class, RETURNS_DEEP_STUBS);
        when(reviewee.getMember().getId()).thenReturn(revieweeId);
    }

    @DisplayName("팀원 후기 등록")
    @Test
    void addMemberReview() {
        // given
        MemberReviewRequest.Add request = MemberReviewTestFactory.getAddRequest(revieweeId);

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(reviewerMember));
        when(memberRepository.findById(revieweeId)).thenReturn(Optional.of(revieweeMember));

        SideProject project = mock(SideProject.class);
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        Set<ProjectMember> projectMembers = new HashSet<>(List.of(reviewer, reviewee));
        when(project.getProjectMembers()).thenReturn(projectMembers);

        MemberScore memberScore = mock(MemberScore.class);
        when(memberScoreRepository.findByMember(reviewee.getMember())).thenReturn(Optional.of(memberScore));

        // when
        memberReviewService.addMemberReview(request, currentUser);

        // then
        verify(memberReviewRepository).save(any(MemberReview.class));
        then(memberScore).should().applyReview(anyInt(), any(MemberReview.class));
    }

    @DisplayName("팀원 후기 건너뛰기")
    @Test
    void skipMemberReview() {
        try (MockedStatic<MemberReview> mockedMemberReview = mockStatic(MemberReview.class)) {
            // given
            MemberReviewRequest.Skip request = MemberReviewTestFactory.getSkipRequest(revieweeId);

            when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(reviewerMember));
            when(memberRepository.findById(anyLong())).thenReturn(Optional.of(revieweeMember));

            SideProject sideProject = mock(SideProject.class);
            when(projectRepository.findById(request.getProjectId())).thenReturn(Optional.of(sideProject));

            Set<ProjectMember> projectMembers = new HashSet<>(List.of(reviewer, reviewee));
            when(sideProject.getProjectMembers()).thenReturn(projectMembers);

            MemberReview memberReview = mock(MemberReview.class);
            mockedMemberReview.when(() -> MemberReview.skip(reviewer, reviewee)).thenReturn(memberReview);

            // when
            memberReviewService.skipMemberReview(request, currentUser);

            //then
            verify(memberReviewRepository).save(memberReview);
            mockedMemberReview.verify(() -> MemberReview.skip(reviewer, reviewee));
        }

    }

}