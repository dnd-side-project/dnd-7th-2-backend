package com.dnd.niceteam.domain.memberscore;

import com.dnd.niceteam.domain.code.ReviewTag;
import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.review.MemberReview;
import com.dnd.niceteam.domain.review.MemberReviewTag;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.*;
import static java.util.Objects.isNull;

@Entity
@Getter
@Builder
@Table(name = "member_score")
@Where(clause = "use_yn = true")
@SQLDelete(sql = "UPDATE member_score SET use_yn = false where member_score_id = ?")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberScore extends BaseEntity {

    private static final int MAX_LEVEL = 4;

    private static final int[] SCORE_TO_FEED_NUM = {0, 0, 0, 1, 2, 2};

    private static final int LEVEL_UP_FEEDS_NUM = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_score_id")
    private Long id;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "review_num", nullable = false)
    private Integer reviewNum;

    @Column(name = "total_participation_score", nullable = false)
    private Integer totalParticipationScore;

    @Column(name = "total_team_again_score", nullable = false)
    private Integer totalTeamAgainScore;

    @Column(name = "total_feeds", nullable = false)
    @Builder.Default
    private Double totalFeeds = 0D;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "review_tag_num", joinColumns = @JoinColumn(name = "member_score_id", nullable = false))
    @MapKeyColumn(name = "review_tag", length = 45)
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "num", nullable = false)
    @Builder.Default
    private Map<ReviewTag, Integer> reviewTagToNums = IntStream.range(0, ReviewTag.values().length).boxed()
                    .collect(Collectors.toMap(i -> ReviewTag.values()[i], i -> 0));

    public static MemberScore createInitMemberScore() {
        return MemberScore.builder()
                .level(1)
                .reviewNum(0)
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
                .totalFeeds(0D)
                .reviewTagToNums(IntStream.range(0, ReviewTag.values().length).boxed()
                        .collect(Collectors.toMap(i -> ReviewTag.values()[i], i -> 0)))
                .build();
    }

    public Double participationPct() {
        if (isNull(totalParticipationScore) || isNull(reviewNum)) {
            return null;
        }
        return (double) totalParticipationScore / reviewNum * 20;
    }

    public void applyReview(int totalProjectMemberNum, MemberReview review) {
        if (Boolean.TRUE.equals(review.getSkipped())) {
            return;
        }
        reviewNum += 1;
        // 한 프로젝트에서 총 얻을 수 있는 점수 비율 / 인원 수
        double feedWeight = (log(totalProjectMemberNum) / log(2)) / max(totalProjectMemberNum - 1, 1);
        int participationScore = review.getParticipationScore();
        int teamAgainScore = review.getTeamAgainScore();
        int feedNum = calculateFeedNum(participationScore, teamAgainScore);

        totalParticipationScore += participationScore;
        totalTeamAgainScore += teamAgainScore;
        totalFeeds += feedWeight * feedNum;
        level = min((int) (totalFeeds / LEVEL_UP_FEEDS_NUM) + 1, MAX_LEVEL);

        review.getMemberReviewTags().stream()
                .map(MemberReviewTag::getTag)
                .forEach(reviewTag -> reviewTagToNums.put(reviewTag,
                        reviewTagToNums.getOrDefault(reviewTag, 0) + 1));
    }

    private int calculateFeedNum(int participationScore, int teamAgainScore) {
        return SCORE_TO_FEED_NUM[min(max(participationScore, 0), SCORE_TO_FEED_NUM.length - 1)] +
                SCORE_TO_FEED_NUM[min(max(teamAgainScore, 0), SCORE_TO_FEED_NUM.length - 1)];
    }
}
