package com.dnd.niceteam.domain.review;

import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.project.ProjectMember;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "member_review")
@SQLDelete(sql = "UPDATE member_review SET use_yn = false WHERE id = ?")
@Where(clause = "use_yn = true")
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

    @Column(name = "skipped", nullable = false)
    private Boolean skipped;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private ProjectMember reviewer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reviewee_id", nullable = false)
    private ProjectMember reviewee;

    @OneToMany(mappedBy = "memberReview", cascade = CascadeType.REMOVE)
    private Set<MemberReviewTag> memberReviewTags = new HashSet<>();

    @Builder
    private MemberReview(
            Integer participationScore,
            Integer hopeToReunionScore,
            ProjectMember reviewer,
            ProjectMember reviewee,
            Set<MemberReviewTag> memberReviewTags
    ) {
        this.participationScore = participationScore;
        this.hopeToReunionScore = hopeToReunionScore;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.memberReviewTags = memberReviewTags;
    }

}
