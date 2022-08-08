package com.dnd.niceteam.domain.university;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UniversityRepository extends JpaRepository<University, Long> {

    List<University> findAllByNameContaining(String name);
}
