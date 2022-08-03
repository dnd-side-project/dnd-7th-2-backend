package com.dnd.niceteam.university.service;

import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import com.dnd.niceteam.university.dto.UniversityDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestJpaConfig.class, UniversityService.class})
@Transactional
class UniversityServiceTest {

    @Autowired
    private UniversityService universityService;

    @Autowired
    private UniversityRepository universityRepository;

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

        // when
        List<UniversityDto> universities = universityService.getUniversityList("스트");

        // then
        assertThat(universities).hasSize(2);
        assertThat(universities).extracting("name")
                .containsExactly("테스트1대학교", "테스트2대학교");
    }
}