package com.dnd.niceteam.domain.project;

import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Table(name = "project_member", uniqueConstraints = { @UniqueConstraint(columnNames = { "project_id", "member_id" }) })
@SQLDelete(sql = "UPDATE project_member SET use_yn = false WHERE project_member_id = ?")
@Where(clause = "use_yn = true")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_member_id")
    private Long id;

    @Column(name = "expelled", nullable = false)
    private Boolean expelled = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    private ProjectMember(Project project, Member member) {
        this.project = project;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectMember that = (ProjectMember) o;
        return Objects.equals(project.getId(), that.project.getId()) && Objects.equals(member.getId(), that.member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(project.getId(), member.getId());
    }

}
