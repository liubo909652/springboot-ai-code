package com.example.springbootaicode.mapper;

import com.example.springbootaicode.model.Clazz;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClazzMapper {

    Clazz findById(@Param("id") Long id);

    List<Clazz> findByCondition(
            @Param("className") String className,
            @Param("grade") String grade,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    long countByCondition(
            @Param("className") String className,
            @Param("grade") String grade
    );

    int insert(Clazz clazz);

    int update(Clazz clazz);

    int deleteById(@Param("id") Long id);
}
