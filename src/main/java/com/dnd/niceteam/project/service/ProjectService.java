package com.dnd.niceteam.project.service;

import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.department.exception.DepartmentNotFoundException;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.LectureProjectRepository;
import com.dnd.niceteam.domain.project.SideProject;
import com.dnd.niceteam.domain.project.SideProjectRepository;
import com.dnd.niceteam.project.dto.ProjectRequest;
import com.dnd.niceteam.project.dto.ProjectResponse;
import com.dnd.niceteam.project.exception.InvalidProjectSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final LectureProjectRepository lectureSideProjectRepository;
    private final SideProjectRepository sideProjectRepository;
    private final DepartmentRepository departmentRepository;
    
    // TODO: 기획 논의 후 startDate, endDate에 @Past, @Future 등의 유효성검사 어노테이션 붙이기
    @Transactional
    public ProjectResponse.Detail registerProject(ProjectRequest.Register request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        if (endDate.isBefore(startDate)) {
            throw new InvalidProjectSchedule("startDate = " + startDate + ", endDate = " + endDate);
        }

        return saveProject(request);
    }

    /* private 메서드 */
    private ProjectResponse.Detail saveProject(ProjectRequest.Register request) {
        if (request.getType() == Type.LECTURE) {
            return ProjectResponse.Detail.from(saveLectureProject(request));
        } else {
            return ProjectResponse.Detail.from(saveSideProject(request));
        }
    }

    /* JPA 메서드 */
    private LectureProject saveLectureProject(ProjectRequest.Register request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("id = " + request.getDepartmentId()));

        LectureProject lectureProject = request.toLectureProject(department);
        return lectureSideProjectRepository.save(lectureProject);
    }

    private SideProject saveSideProject(ProjectRequest.Register request) {
        SideProject sideProject = request.toSideProject();
        return sideProjectRepository.save(sideProject);
    }

}
