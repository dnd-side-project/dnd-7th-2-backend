package com.dnd.niceteam.vote.service;

import com.dnd.niceteam.domain.code.VoteType;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectMember;
import com.dnd.niceteam.domain.project.ProjectRepository;
import com.dnd.niceteam.domain.project.ProjectStatus;
import com.dnd.niceteam.domain.vote.*;
import com.dnd.niceteam.member.util.MemberUtils;
import com.dnd.niceteam.project.exception.ProjectAlreadyDoneException;
import com.dnd.niceteam.project.exception.ProjectMemberNotFoundException;
import com.dnd.niceteam.project.exception.ProjectNotFoundException;
import com.dnd.niceteam.vote.dto.VoteRequest;
import com.dnd.niceteam.vote.exception.*;
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
    private final VoteGroupToCompleteProjectRepository voteGroupToCompleteProjectRepository;
    private final VoteRepository voteRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void addVote(VoteRequest.Add request, User currentUser) {
        Member currentMember =              MemberUtils.findMemberByEmail(currentUser.getUsername(), memberRepository);

        Project project =                   findProject(request.getProjectId());
        ProjectMember voter =               findProjectMemberFrom(project, currentMember.getId());

        if (request.getType() == VoteType.EXPEL)                    voteToExpelCandidate(project, voter, request);
        else if (request.getType() == VoteType.PROJECT_COMPLETE)    voteToCompleteProject(project, voter, request);
    }

    /* private 메서드 */
    // 비즈니스 로직
    private void voteToExpelCandidate(Project project, ProjectMember voter, VoteRequest.Add request) {
        ProjectMember candidate =                           findProjectMemberFrom(project, request.getCandidateMemberId());
        VoteGroupToExpel voteGroup =                        findOrCreateVoteGroupToExpel(project, candidate);

        Long projectId =                                    project.getId();
        Long voteGroupId =                                  voteGroup.getId();
        Long voterId =                                      voter.getId();
        Long voterMemberId =                                voter.getMember().getId();
        Long candidateMemberId =                            request.getCandidateMemberId();

        if (areNotEnoughProjectMembers(project))            throw new NotEnoughProjectMembersException(projectId);
        if (isSelfVote(voterMemberId, candidateMemberId))   throw new SelfVoteException(voterMemberId);
        if (isVoteGroupExpired(voteGroup))                  throw new ExpiredVoteGroupException(voteGroupId);
        if (isVoteGroupAlreadyVoted(voteGroup, voter))      throw new AlreadyVotedException(voteGroupId, voterId);

        saveVoteAndCheckComplete(voteGroup, voter, request);
    }

    private void voteToCompleteProject(Project project, ProjectMember voter, VoteRequest.Add request) {
        VoteGroupToCompleteProject voteGroup =              findOrCreateVoteGroupToCompleteProject(project);

        Long projectId =                                    project.getId();
        Long voteGroupId =                                  voteGroup.getId();
        Long voterId =                                      voter.getId();

        if (isProjectDone(project))                         throw new ProjectAlreadyDoneException(projectId);
        if (isVoteGroupAlreadyVoted(voteGroup, voter))      throw new AlreadyVotedException(voteGroupId, voterId);

        saveVoteAndCheckComplete(voteGroup, voter, request);
    }

    // 예외 처리 메서드
    // 예외 처리 메서드 : 내보내기
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

    // 예외 처리 메서드 : 팀플 완료
    private boolean isProjectDone(Project project) {
        return project.getStatus() == ProjectStatus.DONE;
    }

    // 공통 메서드
    private ProjectMember findProjectMemberFrom(Project project, Long memberId) {
        return project.getProjectMembers().stream()
                .filter(projectMember -> Objects.equals(projectMember.getMember().getId(), memberId))
                .findAny()
                .orElseThrow(() ->  new ProjectMemberNotFoundException(project.getId(), memberId));
    }

    /* JPA 메서드 */
    private Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
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

    private VoteGroupToCompleteProject findOrCreateVoteGroupToCompleteProject(Project project) {
        return voteGroupToCompleteProjectRepository.findByProject(project)
                .orElseGet(() ->
                        voteGroupToCompleteProjectRepository.save(
                                VoteGroupToCompleteProject.builder()
                                        .project(project)
                                        .build()
                        )
                );
    }

    private void saveVoteAndCheckComplete(VoteGroup voteGroup, ProjectMember voter, VoteRequest.Add request) {
        Vote vote = request.toEntity(voteGroup, voter);
        voteRepository.save(vote);

        voteGroup.checkComplete();
    }

}
