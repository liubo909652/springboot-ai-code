package com.example.springbootaicode.controller;

import com.example.springbootaicode.dto.*;
import com.example.springbootaicode.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Validated
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<PageResponse<StudentResponse>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String studentNo,
            @RequestParam(required = false) Long classId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageQuery pageQuery = new PageQuery(page, size);
        return ResponseEntity.ok(studentService.list(name, studentNo, classId, pageQuery));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getById(id));
    }

    @PostMapping
    public ResponseEntity<StudentResponse> create(@RequestBody @Valid CreateStudentRequest request) {
        StudentResponse response = studentService.create(request);
        URI location = URI.create("/api/v1/students/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateStudentRequest request
    ) {
        return ResponseEntity.ok(studentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
