package com.dnd.niceteam.vote;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
import com.dnd.niceteam.domain.code.VoteType;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.SideProject;
import com.dnd.niceteam.vote.dto.VoteRequest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;

public class VoteTestFactory {

    public static VoteRequest.Add createVoteToCompleteAddRequest() {
        VoteRequest.Add dto = new VoteRequest.Add();

        dto.setType(VoteType.PROJECT_COMPLETE);
        dto.setProjectId(1L);
        dto.setCandidateMemberId(2L);
        dto.setChoice(true);

        return dto;
    }

    public static User createUserDetails() {
        return (User) User.withUsername("test@gmail.com")
                .password("0000")
                .authorities(AuthorityUtils.NO_AUTHORITIES)
                .build();
    }

    public static Member createMember(Long id) {
        return Member.builder()
                .id(id)
                .build();
    }

    public static Project createProject(Member currentMember, Member teamMember) {
        Project project = SideProject.builder()
                .name("프로젝트")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(2))
                .field(Field.DESIGN)
                .fieldCategory(FieldCategory.CLUB)
                .build();

        project.addMember(currentMember);
        project.addMember(teamMember);

        return project;
    }


}
