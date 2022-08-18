package com.dnd.niceteam.domain.vote;

import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectMember;
import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("EXPEL")
@OnDelete(action = OnDeleteAction.CASCADE)
public class VoteGroupToExpel extends VoteGroup {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_id", nullable = false)
    private ProjectMember candidate;

    @Builder
    public VoteGroupToExpel(Project project, ProjectMember candidate) {
        super(project);

        Assert.notNull(candidate, "projectMember는 필수 값입니다.");

        this.candidate = candidate;
    }

    /* @Override 메서드 */
    @Override
    protected boolean isVoteCompleted() {
        int numOfMembers = this.getProject().getProjectMembers().size();
        int numOfVotes = this.getVotes().size();

        return numOfMembers - 1 == numOfVotes;
    }

}
