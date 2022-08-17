package com.dnd.niceteam.domain.project;

import com.dnd.niceteam.comment.EntityFactoryForTest;
import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.code.DayOfWeek;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Import(TestJpaConfig.class)
class LectureProjectRepositoryTest {
    @Autowired
    UniversityRepository univerSityRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    LectureProjectRepository lectureProjectRepository;
    @Autowired
    private EntityManager em;

    @Test
    public void addLectureProject() {
        //given
        University university = univerSityRepository.save(EntityFactoryForTest.createUniversity());
        Department department = departmentRepository.save(EntityFactoryForTest.createDepartment(university));

        LectureProject createdLectureProject = lectureProjectRepository.save(
                LectureProject.builder()
                        .name("lecture-project-name")
                        .department(department)
                        .startDate(LocalDate.of(2022, 7, 4))
                        .endDate(LocalDate.of(2022, 8, 28))
                        .professor("test-professor")
                        .lectureTimes(Set.of(LectureTime.builder()
                                .dayOfWeek(DayOfWeek.MON)
                                .startTime(LocalTime.of(9, 30)).build()))
                        .build()
        );

        em.flush();
        em.clear();

        //when
        LectureProject lectureProject = lectureProjectRepository.findById(createdLectureProject.getId())
                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 생성되지 않음."));

        //then
        assertThat(lectureProject.getProfessor()).isEqualTo("test-professor");
    }
}