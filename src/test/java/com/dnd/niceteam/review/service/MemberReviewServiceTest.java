package com.dnd.niceteam.review.service;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectMember;
import com.dnd.niceteam.domain.project.ProjectMemberRepository;
import com.dnd.niceteam.domain.project.ProjectRepository;
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
    ProjectRepository projectRepository;
    @Mock
    ProjectMemberRepository projectMemberRepository;

    @DisplayName("팀원 후기 등록")
    @Test
    void addMemberReview() {
        // given
        Long currentMemberId = 1L;
        Long revieweeId = 2L;
        MemberReviewRequest.Add request = MemberReviewTestFactory.getMemberReviewRequest(revieweeId);

        Member currentMember = mock(Member.class);
        when(currentMember.getId()).thenReturn(currentMemberId);

        Project project = mock(Project.class);
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        List<ProjectMember> projectMembers = getMockProjectMembers(List.of(currentMemberId, revieweeId));
        when(projectMemberRepository.findByProject(project)).thenReturn(projectMembers);

        // when
        memberReviewService.addMemberReview(request, currentMember);

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