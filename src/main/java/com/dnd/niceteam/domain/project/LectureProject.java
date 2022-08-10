package com.dnd.niceteam.domain.project;

import com.dnd.niceteam.domain.department.Department;
import io.jsonwebtoken.lang.Assert;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "lecture_project")
@NoArgsConstructor
@DiscriminatorValue("LECTURE")
public class LectureProject extends Project {

    @Column(length = 50, nullable = false)
    private String professor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ElementCollection
    @CollectionTable(
            name="lecture_time",
            joinColumns = @JoinColumn(name= "project_id", nullable = false)
    )
    private Set<LectureTime> lectureTimes = new HashSet<>();

    @Builder
    private LectureProject(
            String name,
            LocalDate startDate,
            LocalDate endDate,
            String professor,
            Department department,
            Set<LectureTime> lectureTimes
    ) {
        super(name, startDate, endDate);
        Assert.hasText(professor, "professor은 필수 값입니다.");
        Assert.notNull(department, "department는 필수 값입니다.");
        Assert.notEmpty(lectureTimes, "lectureTimes는 필수 값입니다.");

        this.professor = professor;
        this.department = department;
        this.lectureTimes = lectureTimes;
    }
}