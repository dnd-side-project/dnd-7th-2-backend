package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.code.ActivityArea;
import com.dnd.niceteam.domain.code.Personality;
import com.dnd.niceteam.domain.code.ProgressStatus;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.project.Project;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@SQLDelete(sql = "UPDATE recruiting SET use_yn = false WHERE recruiting_id = ?")
@Where(clause = "use_yn = true")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "recruiting")
public class Recruiting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiting_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String content;

    @Column(name = "recruiting_member_count", nullable = false)
    private Integer recruitingMemberCount;

    @Column(name = "recruiting_type", nullable = false)
    private Type recruitingType;

    @Column(name = "activity_area", length = 10, nullable = false)
    private ActivityArea activityArea;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ProgressStatus status = ProgressStatus.IN_PROGRESS;

    @Column(name = "recruiting_end_date")
    private LocalDate recruitingEndDate;

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount;

    @Column(name = "bookmark_count", nullable = false)
    private Integer bookmarkCount;

    @Column(name = "pool_up_count", nullable = false)
    private Integer poolUpCount;

    @Column(name = "pool_up_date")
    private LocalDateTime poolUpDate;

    @Column(name = "intro_link")
    private String introLink;

    @Builder.Default
    @ElementCollection
    @CollectionTable(
            name="recruiting_personality",
            joinColumns = @JoinColumn(name= "recruiting_id"))
    private Set<Personality> personalities = new HashSet<>();

    public void plusCommentCount() {
        this.commentCount += 1;
    }

    public void plusBookmarkCount() {
        this.bookmarkCount += 1;
    }

    public void minusBookmarkCount() {
        this.bookmarkCount -= 1;
    }
}