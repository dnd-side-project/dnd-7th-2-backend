package com.dnd.niceteam.vote.service;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectRepository;
import com.dnd.niceteam.domain.vote.Vote;
import com.dnd.niceteam.domain.vote.VoteGroupToCompleteProject;
import com.dnd.niceteam.domain.vote.VoteGroupToCompleteProjectRepository;
import com.dnd.niceteam.domain.vote.VoteRepository;
import com.dnd.niceteam.vote.VoteTestFactory;
import com.dnd.niceteam.vote.dto.VoteRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @InjectMocks
    VoteService voteService;

    @Mock
    VoteRepository voteRepository;
    @Mock
    VoteGroupToCompleteProjectRepository voteGroupToCompleteProjectRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    ProjectRepository projectRepository;

    @DisplayName("팀플 완료 투표")
    @Test
    void voteToCompleteProject() {
        //given
        VoteRequest.Add request = VoteTestFactory.createVoteToCompleteAddRequest();
        User currentUser = VoteTestFactory.createUserDetails();

        Member currentMember = VoteTestFactory.createMember(1L);
        when(memberRepository.findByEmail(currentUser.getUsername())).thenReturn(Optional.of(currentMember));
        Member teamMember = VoteTestFactory.createMember(2L);

        Project project = VoteTestFactory.createProject(currentMember, teamMember);
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        VoteGroupToCompleteProject voteGroupToCompleteProject = mock(VoteGroupToCompleteProject.class);
        when(voteGroupToCompleteProjectRepository.findByProject(any(Project.class)))
                .thenReturn(Optional.of(voteGroupToCompleteProject));

        // when
        voteService.addVote(request, currentUser);

        // then
        verify(voteRepository).save(any(Vote.class));
    }

}