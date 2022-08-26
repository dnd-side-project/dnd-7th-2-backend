package com.dnd.niceteam.domain.comment;

import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Builder
@SQLDelete(sql = "UPDATE comment SET use_yn = false WHERE comment_id = ?")
@Where(clause = "use_yn = true")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recruiting_id", nullable = false)
    private Recruiting recruiting;

    @Column(nullable = false)
    private String content;

    @Setter
    @Builder.Default
    @Column(name = "parent_id", nullable = false)
    private Long parentId = 0L;

    @Setter
    @Column(name = "group_no")
    private Long groupNo;

    public void update(String content) {
        this.content = content;
    }
}
