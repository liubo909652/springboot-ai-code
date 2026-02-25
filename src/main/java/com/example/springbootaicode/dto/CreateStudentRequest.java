package com.example.springbootaicode.dto;

import jakarta.validation.constraints.*;

public record CreateStudentRequest(
        @NotBlank(message = "学号不能为空")
        @Size(max = 50, message = "学号不超过50个字符")
        String studentNo,

        @NotBlank(message = "姓名不能为空")
        @Size(max = 100, message = "姓名不超过100个字符")
        String name,

        @Min(value = 0, message = "性别值无效") @Max(value = 1, message = "性别值无效")
        Integer gender,

        @Min(value = 1, message = "年龄必须大于0") @Max(value = 150, message = "年龄超出范围")
        Integer age,

        @Size(max = 20, message = "手机号不超过20个字符")
        String phone,

        @Email(message = "邮箱格式不正确")
        @Size(max = 100, message = "邮箱不超过100个字符")
        String email,

        Long classId
) {
}
