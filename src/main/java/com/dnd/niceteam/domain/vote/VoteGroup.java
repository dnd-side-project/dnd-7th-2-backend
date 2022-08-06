package com.dnd.niceteam.domain.vote;

import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.project.Project;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "vote_group")
@SQLDelete(sql = "UPDATE vote_group SET use_yn = false WHERE id = ?")
@Where(clause = "use_yn = true")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_group_id")
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private VoteType type;

    @Column(name = "complete", nullable = false)
    @Builder.Default
    private Boolean complete = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

}
