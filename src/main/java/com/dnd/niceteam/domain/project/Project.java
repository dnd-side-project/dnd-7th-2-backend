package com.dnd.niceteam.domain.project;

import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "project")
@SQLDelete(sql = "UPDATE project SET use_yn = false WHERE project_id = ?")
@Where(clause = "use_yn = true")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@ToString
public abstract class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.NOT_STARTED;

    @Column(name = "member_count", nullable = false)
    private Integer memberCount = 0;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private final Set<ProjectMember> projectMembers = new HashSet<>();

    protected Project(@NonNull String name, @NonNull LocalDate startDate, @NonNull LocalDate endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setName(String name) {
        if (name != null) this.name = name;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate != null) this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate != null) this.endDate = endDate;
    }

    public void start() {
        this.status = ProjectStatus.IN_PROGRESS;
    }

    public void end() {
        this.status = ProjectStatus.DONE;
    }

    public void addMember(ProjectMember projectMember) {
        projectMembers.add(projectMember);
        memberCount += 1;
    }

    public boolean hasMember(Member member) {
        ProjectMember projectMember = ProjectMember.builder()
                .member(member)
                .project(this)
                .build();
        return projectMembers.contains(projectMember);
    }

}
