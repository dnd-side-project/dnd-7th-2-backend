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

@Entity
@Getter
@Table(name = "project_member")
@SQLDelete(sql = "UPDATE project_member SET use_yn = false WHERE id = ?")
@Where(clause = "use_yn = true")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_member_id")
    private Long id;

    @Column(name = "expelled", nullable = false)
    private Boolean expelled = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * 양방향 관계 및<br>
     * Project 변경이 발생할 수 없는<br>
     * ProjectMember의 특징을 고려해<br>
     * Project에서만 생성할 수 있도록 스코프를 제한했습니다.
     * 
     * @param project 등록할 프로젝트
     * @param member 프로젝트에 등록할 회원
     */
    @Builder(access = AccessLevel.PACKAGE)
    private ProjectMember(Project project, Member member) {
        this.project = project;
        this.member = member;
    }

}
