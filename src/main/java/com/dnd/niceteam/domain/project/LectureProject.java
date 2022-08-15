package com.dnd.niceteam.domain.project;

import com.dnd.niceteam.domain.department.Department;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "lecture_project")
@NoArgsConstructor
@DiscriminatorValue("LECTURE")
@ToString(callSuper = true)
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
            @NonNull String professor,
            @NonNull Department department,
            @NonNull Set<LectureTime> lectureTimes
    ) {
        super(name, startDate, endDate);

        this.professor = professor;
        this.department = department;
        this.lectureTimes = lectureTimes;
    }

    public void setProfessor(String professor) {
        if (professor != null) this.professor = professor;
    }

    public void setDepartment(Department department) {
        if (department != null) this.department = department;
    }

    public void setLectureTimes(Set<LectureTime> lectureTimes) {
        if (lectureTimes != null) this.lectureTimes = lectureTimes;
    }
}