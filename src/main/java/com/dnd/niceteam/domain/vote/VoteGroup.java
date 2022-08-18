package com.dnd.niceteam.domain.vote;

import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.project.Project;
import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "vote_group")
@SQLDelete(sql = "UPDATE vote_group SET use_yn = false WHERE id = ?")
@Where(clause = "use_yn = true")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class VoteGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_group_id")
    private Long id;

    @Column(name = "complete", nullable = false)
    private Boolean complete = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "voteGroup")
    private final List<Vote> votes = new ArrayList<>();

    protected VoteGroup(Project project) {
        Assert.notNull(project, "project는 필수 값입니다.");

        this.project = project;
    }

    public void addVote(Vote vote) {
        this.votes.add(vote);
    }

    public void checkComplete() {
        if (isVoteCompleted()) complete = true;
    }

    protected abstract boolean isVoteCompleted();

}
