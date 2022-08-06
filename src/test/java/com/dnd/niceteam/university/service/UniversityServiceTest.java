package com.dnd.niceteam.university.service;

import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import com.dnd.niceteam.university.dto.DepartmentDto;
import com.dnd.niceteam.university.dto.UniversityDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestJpaConfig.class, UniversityService.class})
@Transactional
class UniversityServiceTest {

    @Autowired
    private UniversityService universityService;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EntityManager em;

    @Test
    void getUniversityList() {
        // given
        universityRepository.saveAll(List.of(
                University.builder()
                        .name("테스트1대학교")
                        .emailDomain("test1.ac.kr")
                        .build(),
                University.builder()
                        .name("테스트2대학교")
                        .emailDomain("test2.ac.kr")
                        .build(),
                University.builder()
                        .name("확인용대학교")
                        .emailDomain("check.ac.kr")
                        .build()
        ));
        em.flush();
        em.clear();

        // when
        List<UniversityDto> universities = universityService.getUniversityList("스트");

        // then
        assertThat(universities).hasSize(2);
        assertThat(universities).extracting("name")
                .containsExactly("테스트1대학교", "테스트2대학교");
    }

    @Test
    void getDepartmentsOfUniversity() {
        // given
        University university1 = universityRepository.save(University.builder()
                .name("테스트1대학교")
                .emailDomain("test1.ac.kr")
                .build());
        University university2 = universityRepository.save(University.builder()
                .name("테스트2대학교")
                .emailDomain("test2.ac.kr")
                .build());
        List<University> universities = List.of(university1, university2);
        IntStream.range(0, 4).forEach(i -> {
            departmentRepository.save(Department.builder()
                    .university(universities.get(i % universities.size()))
                    .collegeName("단과대학")
                    .name("학과-" + (i + 1))
                    .mainBranchType("본교")
                    .region("서울")
                    .build());
        });
        em.flush();
        em.clear();

        // when
        List<DepartmentDto> departmentDtos = universityService.getDepartmentsOfUniversity(
                university1.getId(), "학과");

        // then
        assertThat(departmentDtos).hasSize(2);
        assertThat(departmentDtos).extracting("name")
                .containsExactly("학과-1", "학과-3");
    }
}