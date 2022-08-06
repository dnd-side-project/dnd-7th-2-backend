package com.dnd.niceteam.domain.vote;


import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.project.ProjectMember;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "vote")
@SQLDelete(sql = "UPDATE vote SET use_yn = false WHERE id = ?")
@Where(clause = "use_yn = true")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long id;

    @Column(name = "choice", nullable = false)
    private Boolean choice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vote_group_id", nullable = false)
    private VoteGroup voteGroup;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_member_id", nullable = false)
    private ProjectMember projectMember;

}
