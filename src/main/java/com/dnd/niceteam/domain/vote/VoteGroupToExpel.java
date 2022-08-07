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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_member_id", nullable = false)
    private ProjectMember projectMember;

    @Builder
    public VoteGroupToExpel(Project project, ProjectMember projectMember) {
        super(project);

        Assert.notNull(projectMember, "projectMember는 필수 값입니다.");

        this.projectMember = projectMember;
    }

}
