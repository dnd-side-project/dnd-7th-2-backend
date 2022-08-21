package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.code.*;
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

    @Builder.Default
    @Column(name = "intro_link", nullable = false)
    private String introLink = "";

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name="recruiting_personality_adjective",
            joinColumns = @JoinColumn(name= "recruiting_id", nullable = false))
    @Column(name = "adjective", length = 25, nullable = false)
    private Set<Personality.Adjective> personalityAdjectives = new HashSet<>();

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name="recruiting_personality_noun",
            joinColumns = @JoinColumn(name= "recruiting_id", nullable = false))
    @Column(name = "noun", length = 25, nullable = false)
    private Set<Personality.Noun> personalityNouns = new HashSet<>();

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name="recruiting_activity_day_time",
            joinColumns = @JoinColumn(name= "recruiting_id", nullable = false))
    @Column(name = "activity_day_times", length = 50, nullable = false)
    private Set<ActivityDayTime> activityDayTimes = new HashSet<>();

    public void plusCommentCount() {
        this.commentCount += 1;
    }

    public void updateStatus(ProgressStatus status) {
        this.status = status;
    }

    public void plusBookmarkCount() {
        this.bookmarkCount += 1;
    }

    public void minusBookmarkCount() {
        this.bookmarkCount -= 1;
    }

    public boolean checkRecruiter(Member member) {
        return this.member.equals(member);
    }
}