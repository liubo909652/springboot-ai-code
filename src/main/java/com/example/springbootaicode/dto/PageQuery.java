package com.example.springbootaicode.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PageQuery(
        @Min(1) int page,
        @Min(1) @Max(100) int size
) {
    public PageQuery {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        if (size > 100) size = 100;
    }

    public int offset() {
        return (page - 1) * size;
    }
}
