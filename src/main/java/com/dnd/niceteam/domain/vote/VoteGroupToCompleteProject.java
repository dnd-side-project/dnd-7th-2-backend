package com.dnd.niceteam.domain.vote;

import com.dnd.niceteam.domain.project.Project;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("COMPLETE_PROJECT")
@OnDelete(action = OnDeleteAction.CASCADE)
public class VoteGroupToCompleteProject extends VoteGroup {

    @Builder
    public VoteGroupToCompleteProject(Project project) {
        super(project);
    }

    @Override
    protected boolean isVoteCompleted() {
        int numOfMembers = this.getProject().getProjectMembers().size();
        int numOfVotes = this.getVotes().size();

        return numOfMembers == numOfVotes;
    }

}
