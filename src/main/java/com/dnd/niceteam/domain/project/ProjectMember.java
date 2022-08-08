package com.dnd.niceteam.domain.project;

import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "project_member")
@SQLDelete(sql = "UPDATE project_member SET use_yn = false WHERE id = ?")
@Where(clause = "use_yn = true")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_member_id")
    private Long id;

    @Column(name = "is_kicked", nullable = false)
    @Builder.Default
    private Boolean isKicked = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}
