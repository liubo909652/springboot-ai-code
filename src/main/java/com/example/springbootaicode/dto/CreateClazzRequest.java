package com.example.springbootaicode.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateClazzRequest(
        @NotBlank(message = "班级名称不能为空")
        @Size(max = 100, message = "班级名称不超过100个字符")
        String className,

        @NotBlank(message = "年级不能为空")
        @Size(max = 50, message = "年级不超过50个字符")
        String grade,

        @Size(max = 50, message = "班主任姓名不超过50个字符")
        String teacher,

        @Size(max = 500, message = "描述不超过500个字符")
        String description
) {
}
