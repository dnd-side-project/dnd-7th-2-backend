package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.common.BaseTimeEntity;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectMember;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
@Entity
@Getter
@Builder
@SQLDelete(sql = "UPDATE applicant SET use_yn = false WHERE applicant_id = ?")
@Where(clause = "use_yn = true")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "applicant")
public class Applicant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recruiting_id", nullable = false)
    private Recruiting recruiting;

    @Builder.Default
    @Column(nullable = false)
    private Boolean joined = false;

    public void join(Project project, ProjectMember projectMember) {
        joined = true;
        project.addMember(projectMember);
    }

}
