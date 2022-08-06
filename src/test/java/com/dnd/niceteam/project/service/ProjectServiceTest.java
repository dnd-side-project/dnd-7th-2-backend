package com.dnd.niceteam.project.service;

import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectRepository;
import com.dnd.niceteam.project.ProjectTestFactory;
import com.dnd.niceteam.project.dto.ProjectRequest;
import com.dnd.niceteam.project.dto.ProjectResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @InjectMocks
    ProjectService projectService;

    @Mock
    ProjectRepository projectRepository;

    @DisplayName("신규 프로젝트를 등록한 뒤 저장한 상태값들을 반환합니다.")
    @Test
    void registerProject() {
        // given
        ProjectRequest.Register request = ProjectTestFactory.createRegisterRequest();
        Project newProject = request.toEntity();

        // when
        when(projectRepository.save(any(Project.class))).thenReturn(newProject);
        ProjectResponse.Detail response = projectService.registerProject(request);

        // then
        assertAll(
                () -> assertEquals(response.getId(), newProject.getId()),

                // 프로젝트 상태
                () -> assertEquals(response.getName(), newProject.getName()),
                () -> assertEquals(response.getType(), newProject.getType()),
                () -> assertEquals(response.getStatus(), newProject.getStatus()),

                () -> assertEquals(response.getStartDate(), newProject.getStartDate()),
                () -> assertEquals(response.getEndDate(), newProject.getEndDate())
        );
    }

}