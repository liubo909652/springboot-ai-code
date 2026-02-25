package com.example.springbootaicode.dto;

import com.example.springbootaicode.model.Student;

import java.time.LocalDateTime;

public record StudentResponse(
        Long id,
        String studentNo,
        String name,
        Integer gender,
        Integer age,
        String phone,
        String email,
        Long classId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getStudentNo(),
                student.getName(),
                student.getGender(),
                student.getAge(),
                student.getPhone(),
                student.getEmail(),
                student.getClassId(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }
}
