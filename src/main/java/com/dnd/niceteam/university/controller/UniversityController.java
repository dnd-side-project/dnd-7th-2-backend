package com.dnd.niceteam.university.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.university.dto.DepartmentDto;
import com.dnd.niceteam.university.dto.UniversityDto;
import com.dnd.niceteam.university.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/universities")
@RestController
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;

    @GetMapping
    public ResponseEntity<ApiResult<List<UniversityDto>>> universitySearch(
            @RequestParam(required = false) String name) {
        List<UniversityDto> universityList = Optional.ofNullable(name)
                .map(universityService::getUniversityList)
                .orElse(universityService.getAllUniversityList());
        ApiResult<List<UniversityDto>> apiResult = ApiResult.success(universityList);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/{universityId}/departments")
    public ResponseEntity<ApiResult<List<DepartmentDto>>> departmentListOfUniversity(
            @PathVariable long universityId, @RequestParam String name) {
        List<DepartmentDto> departmentDtos = universityService.getDepartmentsOfUniversity(universityId, name);
        ApiResult<List<DepartmentDto>> apiResult = ApiResult.success(departmentDtos);
        return ResponseEntity.ok(apiResult);
    }
}
