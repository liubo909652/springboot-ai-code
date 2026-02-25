package com.example.springbootaicode.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Clazz {

    private Long id;
    private String className;
    private String grade;
    private String teacher;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
