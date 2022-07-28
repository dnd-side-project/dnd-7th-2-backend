package com.dnd.niceteam.domain.member;

import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.code.Personality;
import com.dnd.niceteam.domain.common.BaseEntity;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.university.University;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "member")
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
}
