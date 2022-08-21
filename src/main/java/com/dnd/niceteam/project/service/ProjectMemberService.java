package com.dnd.niceteam.project.service;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectMember;
import com.dnd.niceteam.domain.project.ProjectMemberRepository;
import com.dnd.niceteam.domain.project.ProjectRepository;
import com.dnd.niceteam.domain.review.MemberReview;
import com.dnd.niceteam.domain.review.MemberReviewRepository;
import com.dnd.niceteam.project.dto.ProjectMemberResponse;
import com.dnd.niceteam.project.exception.ProjectMemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final MemberReviewRepository memberReviewRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public ProjectMemberResponse.Summary getProjectMemberByReviewer(Long projectId, Long targetMemberId, Long currentMemberId) {
        Project project =                   projectRepository.getReferenceById(projectId);
        Member currentMember =              memberRepository.getReferenceById(currentMemberId);
        Member targetMember =               memberRepository.getReferenceById(targetMemberId);

        ProjectMember reviewer =            findProjectMember(project, currentMember);
        ProjectMember reviewee =            findProjectMember(project, targetMember);

        MemberReview memberReview =         findMemberReviewOrNull(reviewer, reviewee);

        if (memberReview != null)           return ProjectMemberResponse.Summary.from(reviewee, memberReview);
        else                                return ProjectMemberResponse.Summary.from(reviewee);
    }

    /* JPA 메서드 */
    private ProjectMember findProjectMember(Project project, Member currentMember) {
        return projectMemberRepository.findByProjectAndMember(project, currentMember)
                .orElseThrow(() -> new ProjectMemberNotFoundException(project.getId(), currentMember.getId()));
    }

    private MemberReview findMemberReviewOrNull(ProjectMember reviewer, ProjectMember reviewee) {
        return memberReviewRepository.findByReviewerAndReviewee(reviewer, reviewee);
    }

}
