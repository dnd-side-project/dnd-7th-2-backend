package com.dnd.niceteam.domain.review;

import com.dnd.niceteam.domain.code.ReviewTag;
import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.project.ProjectMember;
import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "member_review")
@SQLDelete(sql = "UPDATE member_review SET use_yn = false WHERE member_review_id = ?")
@Where(clause = "use_yn = true")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_review_id")
    private Long id;

    @Column(name = "participation_score")
    private Integer participationScore;

    @Column(name = "team_again_score")
    private Integer teamAgainScore;

    @Column(name = "skipped", nullable = false)
    private Boolean skipped = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private ProjectMember reviewer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reviewee_id", nullable = false)
    private ProjectMember reviewee;

    @OneToMany(mappedBy = "memberReview", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private Set<MemberReviewTag> memberReviewTags = new HashSet<>();

    @Builder
    private MemberReview(
            Integer participationScore,
            Integer teamAgainScore,
            ProjectMember reviewer,
            ProjectMember reviewee,
            Set<ReviewTag> reviewTags
    ) {

        Assert.notNull(reviewer, "reviewer는 필수 값입니다.");
        Assert.notNull(reviewee, "reviewee는 필수 값입니다.");

        Set<MemberReviewTag> memberReviewTags = null;

        if (reviewTags != null) {
            memberReviewTags = reviewTags.stream().map(tag ->
                    new MemberReviewTag(tag, this)).collect(Collectors.toSet());
        }

        this.participationScore = participationScore;
        this.teamAgainScore = teamAgainScore;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.memberReviewTags = memberReviewTags;
    }

    public static MemberReview skip(ProjectMember reviewer, ProjectMember reviewee) {
        MemberReview memberReview = MemberReview.builder()
                .reviewer(reviewer)
                .reviewee(reviewee)
                .build();

        memberReview.setSkipped(true);

        return memberReview;
    }

    private void setSkipped(Boolean skipped) {
        this.skipped = skipped;
    }

}
