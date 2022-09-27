package com.dnd.niceteam.university.service;

import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import com.dnd.niceteam.domain.university.exception.UniversityNotFoundException;
import com.dnd.niceteam.university.dto.DepartmentDto;
import com.dnd.niceteam.university.dto.UniversityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;

    private final DepartmentRepository departmentRepository;

    public List<UniversityDto> getAllUniversityList() {
        return universityRepository.findAll()
                .stream()
                .map(UniversityDto::of)
                .collect(Collectors.toList());
    }

    public List<UniversityDto> getUniversityList(String name) {
        return universityRepository.findAllByNameContaining(name).stream()
                .map(UniversityDto::of)
                .collect(Collectors.toList());
    }

    public List<DepartmentDto> getDepartmentsOfUniversity(long universityId, String name) {
        University university = getUniversityById(universityId);
        return departmentRepository.findAllByUniversityAndNameContaining(university, name).stream()
                .map(DepartmentDto::of)
                .collect(Collectors.toList());
    }

    private University getUniversityById(long universityId) {
        return universityRepository.findById(universityId)
                .orElseThrow(() -> new UniversityNotFoundException("universityId = " + universityId));
    }
}
