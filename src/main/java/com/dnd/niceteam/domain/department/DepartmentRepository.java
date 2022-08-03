package com.dnd.niceteam.domain.department;

import com.dnd.niceteam.domain.university.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findAllByUniversity(University university);
}
