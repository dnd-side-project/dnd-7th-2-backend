package com.dnd.niceteam.domain.department;

import com.dnd.niceteam.domain.university.University;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "department")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "university_id")
    private University university;

    @Column(name = "college_name", length = 25, nullable = false)
    private String collegeName;

    @Column(name = "name", length = 25, nullable = false)
    private String name;

    @Column(name = "main_branch_type", length = 15, nullable = false)
    private String mainBranchType;

    @Column(name = "region", length = 15, nullable = false)
    private String region;
}
