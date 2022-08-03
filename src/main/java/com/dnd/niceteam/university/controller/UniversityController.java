package com.dnd.niceteam.university.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.university.dto.UniversityDto;
import com.dnd.niceteam.university.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
