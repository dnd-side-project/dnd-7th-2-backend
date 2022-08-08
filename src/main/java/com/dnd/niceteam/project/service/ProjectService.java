package com.dnd.niceteam.project.service;

import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectRepository;
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

    private final ProjectRepository projectRepository;
    
    // TODO: 기획 논의 후 startDate, endDate에 @Past, @Future 등의 유효성검사 어노테이션 붙이기
    @Transactional
    public ProjectResponse.Detail registerProject(ProjectRequest.Register request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        if (endDate.isBefore(startDate)) {
            throw new InvalidProjectSchedule("startDate = " + startDate + ", endDate = " + endDate);
        }

        Project newProject = projectRepository.save(request.toEntity());
        return ProjectResponse.Detail.from(newProject);
    }

}
