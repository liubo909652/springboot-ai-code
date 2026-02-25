package com.example.springbootaicode.dto;

import com.example.springbootaicode.model.Clazz;

import java.time.LocalDateTime;
import java.util.List;

public record ClazzResponse(
        Long id,
        String className,
        String grade,
        String teacher,
        String description,
        List<StudentResponse> students,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ClazzResponse from(Clazz clazz) {
        return new ClazzResponse(
                clazz.getId(),
                clazz.getClassName(),
                clazz.getGrade(),
                clazz.getTeacher(),
                clazz.getDescription(),
                null,
                clazz.getCreatedAt(),
                clazz.getUpdatedAt()
        );
    }

    public static ClazzResponse from(Clazz clazz, List<StudentResponse> students) {
        return new ClazzResponse(
                clazz.getId(),
                clazz.getClassName(),
                clazz.getGrade(),
                clazz.getTeacher(),
                clazz.getDescription(),
                students,
                clazz.getCreatedAt(),
                clazz.getUpdatedAt()
        );
    }
}
