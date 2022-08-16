package com.dnd.niceteam.review.service;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectMember;
import com.dnd.niceteam.domain.project.ProjectRepository;
import com.dnd.niceteam.domain.review.MemberReview;
import com.dnd.niceteam.domain.review.MemberReviewRepository;
import com.dnd.niceteam.member.util.MemberUtils;
import com.dnd.niceteam.project.exception.ProjectMemberNotFoundException;
import com.dnd.niceteam.project.exception.ProjectNotFoundException;
import com.dnd.niceteam.review.dto.MemberReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberReviewService {

    private final MemberReviewRepository memberReviewRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void addMemberReview(MemberReviewRequest.Add request, User currentUser) {
        ReviewerAndReviewee projectMember = getReviewerAndReviewee(request, currentUser);

        MemberReview newMemberReview = request.toEntity(
                projectMember.reviewer,
                projectMember.reviewee
        );
        memberReviewRepository.save(newMemberReview);
    }

    @Transactional
    public void skipMemberReview(MemberReviewRequest.Skip request, User currentUser) {
        ReviewerAndReviewee projectMember = getReviewerAndReviewee(request, currentUser);

        MemberReview newMemberReview = MemberReview.skip(
                projectMember.reviewer,
                projectMember.reviewee
        );
        memberReviewRepository.save(newMemberReview);
    }

    /* private 메서드 */
    private ReviewerAndReviewee getReviewerAndReviewee(MemberReviewRequest request, User currentUser) {
        // 요청 data
        Long projectId = request.getProjectId();
        Long revieweeId = request.getRevieweeId();

        String reviewerEmail = currentUser.getUsername();

        // DB 조회
        Project project = findProjectById(projectId);

        ProjectMember reviewer = findProjectMemberFrom(project, reviewerEmail);
        ProjectMember reviewee = findProjectMemberFrom(project, revieweeId);

        return new ReviewerAndReviewee(reviewer, reviewee);
    }

    /* DB 조회 메서드 */
    // DB 조회 : Project
    private Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("id = " + projectId));
    }

    // DB 조회 : ProjectMember
    private ProjectMember findProjectMemberFrom(Project project, Long id) {
        Member member = MemberUtils.findMemberById(id, memberRepository);
        return findProjectMemberFrom(project, member);
    }

    private ProjectMember findProjectMemberFrom(Project project, String email) {
        Member member = MemberUtils.findMemberByEmail(email, memberRepository);
        return findProjectMemberFrom(project, member);
    }

    private ProjectMember findProjectMemberFrom(Project project, Member member) {
        return project.getProjectMembers().stream()
                .filter(projectMember -> Objects.equals(projectMember.getMember().getId(), member.getId()))
                .findAny()
                .orElseThrow(() -> new ProjectMemberNotFoundException("memberId = " + member.getId()));
    }

    /* 내부 클래스 */
    private static class ReviewerAndReviewee {
        ProjectMember reviewer;
        ProjectMember reviewee;

        ReviewerAndReviewee(ProjectMember reviewer, ProjectMember reviewee) {
            this.reviewer = reviewer;
            this.reviewee = reviewee;
        }
    }

}
