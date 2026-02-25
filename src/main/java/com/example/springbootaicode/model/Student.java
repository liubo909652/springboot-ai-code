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
public class Student {

    private Long id;
    private String studentNo;
    private String name;
    private Integer gender;
    private Integer age;
    private String phone;
    private String email;
    private Long classId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
