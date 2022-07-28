package com.dnd.niceteam.domain.memberscore;

import com.dnd.niceteam.domain.code.TagReview;
import com.dnd.niceteam.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

@Entity
@Getter
@Builder
@Table(name = "member_score")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberScore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_score_id")
    private Long id;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "review_num", nullable = false)
    private Integer reviewNum;

    @Column(name = "participation_sum", nullable = false)
    private Integer participationSum;

    @Column(name = "rematching_sum", nullable = false)
    private Integer rematchingSum;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tag_review_num", joinColumns = @JoinColumn(name = "member_score_id", nullable = false))
    @MapKeyColumn(name = "tag_review", length = 45)
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "num", nullable = false)
    @Builder.Default
    private Map<TagReview, Integer> tagReviewToNums = new HashMap<>(
            IntStream.range(0, TagReview.values().length).boxed()
                    .collect(Collectors.toMap(i -> TagReview.values()[i], i -> 0))
    );

    public Double participationAvg() {
        if (isNull(participationSum) || isNull(reviewNum)) {
            return null;
        }
        return (double) participationSum / reviewNum;
    }

    public Double rematchingAvg() {
        if (isNull(rematchingSum) || isNull(reviewNum)) {
            return null;
        }
        return (double) rematchingSum / reviewNum;
    }
}
