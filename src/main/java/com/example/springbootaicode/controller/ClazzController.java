package com.example.springbootaicode.controller;

import com.example.springbootaicode.dto.*;
import com.example.springbootaicode.service.ClazzService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/classes")
@RequiredArgsConstructor
@Validated
public class ClazzController {

    private final ClazzService clazzService;

    @GetMapping
    public ResponseEntity<PageResponse<ClazzResponse>> list(
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String grade,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageQuery pageQuery = new PageQuery(page, size);
        return ResponseEntity.ok(clazzService.list(className, grade, pageQuery));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClazzResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clazzService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ClazzResponse> create(@RequestBody @Valid CreateClazzRequest request) {
        ClazzResponse response = clazzService.create(request);
        URI location = URI.create("/api/v1/classes/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClazzResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateClazzRequest request
    ) {
        return ResponseEntity.ok(clazzService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clazzService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
