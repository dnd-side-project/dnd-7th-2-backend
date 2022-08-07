package com.dnd.niceteam.domain.review;

import com.dnd.niceteam.domain.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Builder
@Table(name = "member_review_tag")
@SQLDelete(sql = "UPDATE member_review_tag SET use_yn = false WHERE id = ?")
@Where(clause = "use_yn = true")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReviewTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_review_tag_id")
    private Long id;

    @Column(name = "tag", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberReviewTagName tag;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_review_id", nullable = false)
    private MemberReview memberReview;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberReviewTag that = (MemberReviewTag) o;
        return tag == that.tag && memberReview.equals(that.memberReview);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, memberReview);
    }
}
