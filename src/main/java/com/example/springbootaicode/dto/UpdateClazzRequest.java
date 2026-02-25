package com.example.springbootaicode.dto;

import jakarta.validation.constraints.Size;

public record UpdateClazzRequest(
        @Size(max = 100, message = "班级名称不超过100个字符")
        String className,

        @Size(max = 50, message = "年级不超过50个字符")
        String grade,

        @Size(max = 50, message = "班主任姓名不超过50个字符")
        String teacher,

        @Size(max = 500, message = "描述不超过500个字符")
        String description
) {
}
