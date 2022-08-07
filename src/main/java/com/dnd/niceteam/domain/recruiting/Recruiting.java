package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.code.ActivityArea;
import com.dnd.niceteam.domain.code.Personality;
import com.dnd.niceteam.domain.code.ProgressStatus;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@SQLDelete(sql = "UPDATE recruiting SET use_yn = false WHERE id = ?")
@Where(clause = "use_yn = true")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "recruiting_type")
@Table(name = "recruiting")
public abstract class Recruiting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiting_id")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "project_id", nullable = false)
//    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String content;

    @Column(name = "recruiting_member_count", nullable = false)
    private int recruitingMemberCount;

    @Column(name = "recruiting_type", nullable = false)
    private Type recruitingType;

    @Column(name = "activity_area", length = 10, nullable = false)
    private ActivityArea activityArea;

    @Enumerated(EnumType.STRING)
    private ProgressStatus status;

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount;

    @Column(name = "bookmark_count", nullable = false)
    private Integer bookmarkCount;

    @Column(name = "pool_up_count", nullable = false)
    private Integer poolUpCount;

    @Column(name = "pool_up_date")
    private LocalDate poolUpDate;

    @Column(name = "intro_link")
    private String introLink;

    @ElementCollection
    @CollectionTable(name="recruiting_personality", joinColumns = @JoinColumn(name= "recruiting_id", nullable = false))
    private Set<Personality> personalities = new HashSet<>();

}