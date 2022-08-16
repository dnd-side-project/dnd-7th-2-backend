package com.dnd.niceteam.project.service;

import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.department.exception.DepartmentNotFoundException;
import com.dnd.niceteam.domain.project.*;
import com.dnd.niceteam.project.dto.LectureTimeRequest;
import com.dnd.niceteam.project.dto.ProjectRequest;
import com.dnd.niceteam.project.dto.ProjectResponse;
import com.dnd.niceteam.project.exception.InvalidProjectSchedule;
import com.dnd.niceteam.project.exception.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
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

    @Transactional
    public void modifyProject(Long projectId, ProjectRequest.Update request) {
        Project project = findProject(projectId);

        modifyProject(project, request);
    }

    /* private 메서드 */
    private ProjectResponse.Detail saveProject(ProjectRequest.Register request) {
        if (request.getType() == Type.LECTURE) {
            return ProjectResponse.Detail.from(saveLectureProject(request));
        } else {
            return ProjectResponse.Detail.from(saveSideProject(request));
        }
    }

    private void modifyProject(Project project, ProjectRequest.Update request) {
        project.setName(request.getName());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        if (project.getType() == Type.LECTURE)      modifyLectureProject((LectureProject) project, request);
        else                                        modifySideProject((SideProject) project, request);
    }

    private void modifyLectureProject(LectureProject lectureProject, ProjectRequest.Update request) {
        Department newDepartment =          departmentRepository.getReferenceById(request.getDepartmentId());
        Set<LectureTime> lectureTimeSet =   request.getLectureTimes().stream()
                                                .map(LectureTimeRequest::toEntity).collect(Collectors.toSet());

        lectureProject.setDepartment(newDepartment);
        lectureProject.setProfessor(request.getProfessor());
        lectureProject.setLectureTimes(lectureTimeSet);
    }

    private void modifySideProject(SideProject sideProject, ProjectRequest.Update request) {
        sideProject.setField(request.getField());
        sideProject.setFieldCategory(request.getFieldCategory());
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

    private Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("id = " + projectId));
    }

}
