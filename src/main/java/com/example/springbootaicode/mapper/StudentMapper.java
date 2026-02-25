package com.example.springbootaicode.mapper;

import com.example.springbootaicode.model.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {

    Student findById(@Param("id") Long id);

    List<Student> findByClassId(@Param("classId") Long classId);

    List<Student> findByCondition(
            @Param("name") String name,
            @Param("studentNo") String studentNo,
            @Param("classId") Long classId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    long countByCondition(
            @Param("name") String name,
            @Param("studentNo") String studentNo,
            @Param("classId") Long classId
    );

    long countByClassId(@Param("classId") Long classId);

    boolean existsByStudentNo(@Param("studentNo") String studentNo, @Param("excludeId") Long excludeId);

    int insert(Student student);

    int update(Student student);

    int deleteById(@Param("id") Long id);
}
