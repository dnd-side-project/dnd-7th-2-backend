package com.dnd.niceteam.domain.memberreview;

import com.dnd.niceteam.domain.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "member_review")
@SQLDelete(sql = "UPDATE member_review SET use_yn = false WHERE id = ?")
@Where(clause = "use_yn = true")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_review_id")
    private Long id;

    @Column(name = "participation_score", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private Integer participationScore;

    @Column(name = "hope_to_reunion_score", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private Integer hopeToReunionScore;

    @Column(name = "is_skipped", nullable = false)
    private Boolean isSkipped;

}
