package com.dnd.niceteam.review.service;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectMember;
import com.dnd.niceteam.domain.project.ProjectMemberRepository;
import com.dnd.niceteam.domain.project.ProjectRepository;
import com.dnd.niceteam.domain.review.MemberReview;
import com.dnd.niceteam.domain.review.MemberReviewRepository;
import com.dnd.niceteam.domain.review.MemberReviewTag;
import com.dnd.niceteam.project.exception.ProjectMemberNotFoundException;
import com.dnd.niceteam.project.exception.ProjectNotFoundException;
import com.dnd.niceteam.review.dto.MemberReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberReviewService {

    private final MemberReviewRepository memberReviewRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public MemberReview addMemberReview(MemberReviewRequest.Add request, Member currentMember) {
        Project project = findProject(request.getProjectId());
        List<ProjectMember> projectMembers = projectMemberRepository.findByProject(project);

        ProjectMember reviewer = findProjectMemberFrom(projectMembers, currentMember.getId());
        ProjectMember reviewee = findProjectMemberFrom(projectMembers, request.getRevieweeId());
        Set<MemberReviewTag> tags = getMemberReviewTags(request.getTagNames());

        MemberReview newMemberReview = request.toEntity(reviewer, reviewee, tags);
        return memberReviewRepository.save(newMemberReview);
    }

    private Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("id = " + projectId));
    }

    private ProjectMember findProjectMemberFrom(List<ProjectMember> projectMembers, Long memberId) {
        return projectMembers.stream()
                .filter(projectMember -> Objects.equals(projectMember.getMember().getId(), memberId))
                .findAny()
                .orElseThrow(() -> new ProjectMemberNotFoundException("memberId = " + memberId));
    }

    private Set<MemberReviewTag> getMemberReviewTags(List<String> tagNames) {
        return tagNames.stream().map(MemberReviewTag::new).collect(Collectors.toSet());
    }

}
