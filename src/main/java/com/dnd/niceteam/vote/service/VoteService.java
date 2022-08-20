package com.dnd.niceteam.vote.service;

import com.dnd.niceteam.domain.code.VoteType;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectMember;
import com.dnd.niceteam.domain.project.ProjectRepository;
import com.dnd.niceteam.domain.vote.*;
import com.dnd.niceteam.member.util.MemberUtils;
import com.dnd.niceteam.project.exception.ProjectMemberNotFoundException;
import com.dnd.niceteam.project.exception.ProjectNotFoundException;
import com.dnd.niceteam.vote.dto.VoteRequest;
import com.dnd.niceteam.vote.exception.AlreadyVotedException;
import com.dnd.niceteam.vote.exception.ExpiredVoteGroupException;
import com.dnd.niceteam.vote.exception.NotEnoughProjectMembersException;
import com.dnd.niceteam.vote.exception.SelfVoteException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

    private static final int VOTE_DURATION = 7;

    private final VoteGroupToExpelRepository voteGroupToExpelRepository;
    private final VoteRepository voteRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void addVote(VoteRequest.Add request, User currentUser) {
        Member currentMember =              MemberUtils.findMemberByEmail(currentUser.getUsername(), memberRepository);

        Project project =                   findProject(request.getProjectId());
        ProjectMember voter =               findProjectMemberFrom(project, currentMember.getId());

        if (request.getType() == VoteType.EXPEL)    voteToExpelCandidate(project, voter, request);
    }

    /* private 메서드 */
    // 내보내기 투표 : 비즈니스 로직
    private void voteToExpelCandidate(Project project, ProjectMember voter, VoteRequest.Add request) {
        ProjectMember candidate =           findProjectMemberFrom(project, request.getCandidateMemberId());
        VoteGroupToExpel voteGroup =        findOrCreateVoteGroupToExpel(project, candidate);

        if (areNotEnoughProjectMembers(project))
            throw new NotEnoughProjectMembersException(project.getId());
        if (isSelfVote(voter.getMember().getId(), request.getCandidateMemberId()))
            throw new SelfVoteException(voter.getMember().getId());
        if (isVoteGroupExpired(voteGroup))  throw new ExpiredVoteGroupException(voteGroup.getId());
        if (isVoteGroupAlreadyVoted(voteGroup, voter))
            throw new AlreadyVotedException(voteGroup.getId(), voter.getId());

        Vote vote =                         request.toEntity(voteGroup, voter);
        voteRepository.save(vote);

        voteGroup.checkComplete();
    }

    private VoteGroupToExpel findOrCreateVoteGroupToExpel(Project project, ProjectMember candidate) {
        LocalDateTime validCreatedDate = LocalDateTime.now().minusDays(VOTE_DURATION);

        return voteGroupToExpelRepository.findByProjectAndCandidateAndCreatedDateGreaterThan(project, candidate, validCreatedDate)
                .orElseGet(() ->
                        voteGroupToExpelRepository.save(
                                VoteGroupToExpel.builder()
                                        .project(project)
                                        .candidate(candidate)
                                        .build()
                        )
                );
    }
    
    // 내보내기 투표 : 예외 처리 메서드
    private boolean isSelfVote(Long voterId, Long candidateId) {
        return Objects.equals(voterId, candidateId);
    }

    private boolean areNotEnoughProjectMembers(Project project) {
        return project.getProjectMembers().size() <= 2;
    }

    private boolean isVoteGroupExpired(VoteGroup voteGroup) {
        return voteGroup.getCreatedDate()
                .isBefore(LocalDateTime.now().minusDays(VOTE_DURATION));
    }

    private boolean isVoteGroupAlreadyVoted(VoteGroup voteGroup, ProjectMember voter) {
        return voteRepository.existsByVoteGroupAndVoter(voteGroup, voter);
    }

    // 공통 메서드
    private ProjectMember findProjectMemberFrom(Project project, Long memberId) {
        return project.getProjectMembers().stream()
                .filter(projectMember -> Objects.equals(projectMember.getMember().getId(), memberId))
                .findAny()
                .orElseThrow(() ->  new ProjectMemberNotFoundException(memberId));
    }

    /* JPA 메서드 */
    private Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

}
