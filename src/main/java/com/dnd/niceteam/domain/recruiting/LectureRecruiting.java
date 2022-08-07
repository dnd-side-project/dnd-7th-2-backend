package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.department.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("CLASS")
@PrimaryKeyJoinColumn(name = "class_recruiting_id")
@Table(name = "lecture_recruiting")
public class LectureRecruiting extends Recruiting{
    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(length = 50, nullable = false)
    private String professor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Department department;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name="class_time", joinColumns = @JoinColumn(name= "recruiting_id", nullable = false))
    private Set<LectureTime> classTimes = new HashSet<>();

}