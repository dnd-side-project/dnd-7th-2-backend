package com.dnd.niceteam.project.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.project.ProjectStatus;
import com.dnd.niceteam.project.dto.ProjectMemberRequest;
import com.dnd.niceteam.project.dto.ProjectResponse;
import com.dnd.niceteam.project.service.ProjectService;
import com.dnd.niceteam.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/me")
    public ResponseEntity<ApiResult<Pagination<ProjectResponse.ListItem>>> myProjectList(
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer perSize,
            @RequestParam(required = false) ProjectStatus status,
            @CurrentUser User currentUser
    ) {
        Pagination<ProjectResponse.ListItem> projects = projectService.getProjectList(page, perSize, status, currentUser);
        ApiResult<Pagination<ProjectResponse.ListItem>> apiResult = ApiResult.success(projects);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResult<ProjectResponse.Detail>> projectDetails(
            @PathVariable Long projectId,
            @CurrentUser User currentUser
    ) {
        ProjectResponse.Detail response = projectService.getProject(projectId, currentUser);
        ApiResult<ProjectResponse.Detail> apiResult = ApiResult.success(response);
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/project-members")
    public ResponseEntity<ApiResult<Void>> projectMemberAdd(@RequestBody ProjectMemberRequest.Add request, @CurrentUser User user) {
        projectService.addProjectMember(request, user);
        ApiResult<Void> apiResult = ApiResult.<Void>success().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

}
