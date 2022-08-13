package com.dnd.niceteam.code.controller;

import com.dnd.niceteam.code.config.EnumMapperFactory;
import com.dnd.niceteam.code.dto.EnumMapperValue;
import com.dnd.niceteam.common.dto.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@RequestMapping("/code")
@RestController
@RequiredArgsConstructor
public class CodeController {

    private final EnumMapperFactory enumMapperFactory;

    @GetMapping
    public ResponseEntity<ApiResult<Map<String, List<EnumMapperValue>>>> codeList(
            @RequestParam(required = false) List<String> codeTypes) {
        if (isNull(codeTypes)) {
            ApiResult<Map<String, List<EnumMapperValue>>> apiResult = ApiResult.success(enumMapperFactory.getAll());
            return ResponseEntity.ok(apiResult);
        }
        ApiResult<Map<String, List<EnumMapperValue>>> apiResult = ApiResult.success(enumMapperFactory.get(codeTypes));
        return ResponseEntity.ok(apiResult);
    }
}
