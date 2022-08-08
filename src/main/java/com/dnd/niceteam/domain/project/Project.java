package com.dnd.niceteam.domain.project;

import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.member.Member;
import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "project")
@SQLDelete(sql = "UPDATE project SET use_yn = false WHERE id = ?")
@Where(clause = "use_yn = true")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectType type;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.NOT_STARTED;

    @OneToMany(mappedBy = "project")
    private Set<ProjectMember> projectMembers = new HashSet<>();

    @Builder
    private Project(String name, ProjectType type, LocalDate startDate, LocalDate endDate, ProjectStatus status) {
        Assert.hasText(name, "name은 필수 값입니다.");
        Assert.notNull(type, "type은 필수 값입니다.");
        Assert.notNull(startDate, "startDate는 필수 값입니다.");
        Assert.notNull(endDate, "endDate는 필수 값입니다.");
        Assert.notNull(name, "status는 필수 값입니다.");

        this.name = name;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public void addMember(Member member) {
        ProjectMember projectMember = ProjectMember.builder()
                .project(this)
                .member(member)
                .build();
        projectMembers.add(projectMember);
    }

}
