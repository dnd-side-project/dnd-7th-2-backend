package com.dnd.niceteam.domain.member;

import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.Personality;
import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.memberscore.MemberScore;
import com.dnd.niceteam.domain.university.University;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Builder
@Table(name = "member")
@Where(clause = "use_yn = true")
@SQLDelete(sql = "UPDATE member SET use_yn = false where member_id = ?")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "university_id")
    private University university;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_score_id")
    private MemberScore memberScore;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "member_interest", joinColumns = @JoinColumn(name = "member_id", nullable = false))
    @Enumerated(EnumType.STRING)
    @Column(name = "field", length = 25, nullable = false)
    @Builder.Default
    private Set<Field> interestingFields = new HashSet<>();

    @Column(name = "nickname", length = 25, unique = true, nullable = false)
    private String nickname;

    @Column(name = "admission_year", nullable = false)
    private Integer admissionYear;

    @Embedded
    private Personality personality;

    @Column(name = "introduction", length = 300, nullable = false)
    private String introduction;

    @Column(name = "introduction_url", nullable = false)
    private String introductionUrl;

    public MemberEditor.MemberEditorBuilder toEditor() {
        return MemberEditor.builder()
                .nickname(getNickname())
                .personalityAdjective(getPersonality().getAdjective())
                .personalityNoun(getPersonality().getNoun())
                .interestingFields(new HashSet<>(getInterestingFields()))
                .introduction(getIntroduction())
                .introductionUrl(getIntroductionUrl());
    }

    public Long edit(MemberEditor memberEditor) {
        nickname = memberEditor.getNickname();
        personality = new Personality(
                memberEditor.getPersonalityAdjective(), memberEditor.getPersonalityNoun());
        interestingFields = memberEditor.getInterestingFields();
        introduction = memberEditor.getIntroduction();
        introductionUrl = memberEditor.getIntroductionUrl();
        return getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
