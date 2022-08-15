package com.dnd.niceteam.project.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.project.ProjectStatus;
import com.dnd.niceteam.project.dto.ProjectResponse;
import com.dnd.niceteam.project.service.ProjectService;
import com.dnd.niceteam.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
