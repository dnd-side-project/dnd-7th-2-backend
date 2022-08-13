package com.dnd.niceteam.project.service;

import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.department.exception.DepartmentNotFoundException;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.ProjectRepository;
import com.dnd.niceteam.domain.project.LectureTime;
import com.dnd.niceteam.project.dto.LectureProjectRequest;
import com.dnd.niceteam.project.dto.LectureProjectResponse;
import com.dnd.niceteam.project.dto.LectureTimeRequest;
import com.dnd.niceteam.project.exception.InvalidProjectSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureProjectService {

    private final ProjectRepository<LectureProject> projectRepository;
    private final DepartmentRepository departmentRepository;
    
    // TODO: 기획 논의 후 startDate, endDate에 @Past, @Future 등의 유효성검사 어노테이션 붙이기
    @Transactional
    public LectureProjectResponse.Detail registerLectureProject(LectureProjectRequest.Register request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        if (endDate.isBefore(startDate)) {
            throw new InvalidProjectSchedule("startDate = " + startDate + ", endDate = " + endDate);
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("id = " + request.getDepartmentId()));

        Set<LectureTime> lectureTimes = mapLectureTimeFromRequest(request.getLectureTimes());

        LectureProject newLectureProject = projectRepository.save(
                request.toEntity(department, lectureTimes)
        );
        return LectureProjectResponse.Detail.from(newLectureProject);
    }

    private Set<LectureTime> mapLectureTimeFromRequest(List<LectureTimeRequest> dtos) {
        return dtos.stream().map(LectureTimeRequest::toEntity).collect(Collectors.toSet());
    }

}
