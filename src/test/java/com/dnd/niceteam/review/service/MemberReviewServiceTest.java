package com.dnd.niceteam.review.service;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepositoryCustom;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.LectureProjectRepository;
import com.dnd.niceteam.domain.project.ProjectMember;
import com.dnd.niceteam.domain.project.ProjectMemberRepository;
import com.dnd.niceteam.domain.review.MemberReview;
import com.dnd.niceteam.domain.review.MemberReviewRepository;
import com.dnd.niceteam.review.MemberReviewTestFactory;
import com.dnd.niceteam.review.dto.MemberReviewRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberReviewServiceTest {

    @InjectMocks
    MemberReviewService memberReviewService;

    @Mock
    MemberReviewRepository memberReviewRepository;
    @Mock
    MemberRepositoryCustom memberRepositoryCustom;
    @Mock
    LectureProjectRepository lectureProjectRepository;
    @Mock
    ProjectMemberRepository projectMemberRepository;

    @DisplayName("팀원 후기 등록")
    @Test
    void addMemberReview() {
        // given
        Long currentMemberId = 1L;
        Long revieweeId = 2L;
        String email = "tester@gmail.com";
        MemberReviewRequest.Add request = MemberReviewTestFactory.getMemberReviewRequest(revieweeId);

        User currentUser = mock(User.class);
        when(currentUser.getUsername()).thenReturn(email);

        Member currentMember = mock(Member.class);
        when(currentMember.getId()).thenReturn(currentMemberId);
        when(memberRepositoryCustom.findByEmail(anyString())).thenReturn(Optional.of(currentMember));

        LectureProject lectureProject = mock(LectureProject.class);
        when(lectureProjectRepository.findById(anyLong())).thenReturn(Optional.of(lectureProject));

        ProjectMember reviewer = mock(ProjectMember.class, RETURNS_DEEP_STUBS);
        when(reviewer.getMember().getId()).thenReturn(currentMemberId);

        ProjectMember reviewee = mock(ProjectMember.class, RETURNS_DEEP_STUBS);
        when(reviewee.getMember().getId()).thenReturn(revieweeId);

        List<ProjectMember> projectMembers = new ArrayList<>(List.of(reviewer, reviewee));
        when(projectMemberRepository.findByProject(lectureProject)).thenReturn(projectMembers);

        // when
        memberReviewService.addMemberReview(request, currentUser);

        // then
        verify(memberReviewRepository).save(any(MemberReview.class));
    }

    private List<ProjectMember> getMockProjectMembers(List<Long> ids) {
        return ids.stream().map(id -> {
            ProjectMember projectMember = mock(ProjectMember.class, RETURNS_DEEP_STUBS);
            when(projectMember.getMember().getId()).thenReturn(id);
            return projectMember;
        }).collect(Collectors.toList());
    }

}