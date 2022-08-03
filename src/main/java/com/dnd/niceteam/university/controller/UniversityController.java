package com.dnd.niceteam.university.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.university.dto.DepartmentDto;
import com.dnd.niceteam.university.dto.UniversityDto;
import com.dnd.niceteam.university.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/universities")
@RestController
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;

    @GetMapping
    public ResponseEntity<ApiResult<List<UniversityDto>>> universitySearch(@RequestParam String name) {
        ApiResult<List<UniversityDto>> apiResult = ApiResult.success(universityService.getUniversityList(name));
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/{universityId}/departments")
    public ResponseEntity<ApiResult<List<DepartmentDto>>> departmentListOfUniversity(@PathVariable long universityId) {
        List<DepartmentDto> departmentDtos = universityService.getDepartmentsOfUniversity(universityId);
        ApiResult<List<DepartmentDto>> apiResult = ApiResult.success(departmentDtos);
        return ResponseEntity.ok(apiResult);
    }
}
