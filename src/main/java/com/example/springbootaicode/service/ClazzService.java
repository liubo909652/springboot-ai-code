package com.example.springbootaicode.service;

import com.example.springbootaicode.dto.*;
import com.example.springbootaicode.exception.BusinessException;
import com.example.springbootaicode.exception.EntityNotFoundException;
import com.example.springbootaicode.mapper.ClazzMapper;
import com.example.springbootaicode.mapper.StudentMapper;
import com.example.springbootaicode.model.Clazz;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClazzService {

    private final ClazzMapper clazzMapper;
    private final StudentMapper studentMapper;

    public PageResponse<ClazzResponse> list(String className, String grade, PageQuery pageQuery) {
        List<Clazz> clazzes = clazzMapper.findByCondition(className, grade, pageQuery.offset(), pageQuery.size());
        long total = clazzMapper.countByCondition(className, grade);
        List<ClazzResponse> content = clazzes.stream().map(ClazzResponse::from).toList();
        return PageResponse.of(content, pageQuery.page(), pageQuery.size(), total);
    }

    public ClazzResponse getById(Long id) {
        Clazz clazz = Optional.ofNullable(clazzMapper.findById(id))
                .orElseThrow(() -> new EntityNotFoundException("班级不存在: " + id));
        List<StudentResponse> students = studentMapper.findByClassId(id)
                .stream().map(StudentResponse::from).toList();
        return ClazzResponse.from(clazz, students);
    }

    @Transactional
    public ClazzResponse create(CreateClazzRequest request) {
        Clazz clazz = Clazz.builder()
                .className(request.className())
                .grade(request.grade())
                .teacher(request.teacher())
                .description(request.description())
                .build();
        clazzMapper.insert(clazz);
        return ClazzResponse.from(clazz);
    }

    @Transactional
    public ClazzResponse update(Long id, UpdateClazzRequest request) {
        Clazz existing = Optional.ofNullable(clazzMapper.findById(id))
                .orElseThrow(() -> new EntityNotFoundException("班级不存在: " + id));
        existing.setClassName(request.className() != null ? request.className() : existing.getClassName());
        existing.setGrade(request.grade() != null ? request.grade() : existing.getGrade());
        existing.setTeacher(request.teacher() != null ? request.teacher() : existing.getTeacher());
        existing.setDescription(request.description() != null ? request.description() : existing.getDescription());
        clazzMapper.update(existing);
        return ClazzResponse.from(existing);
    }

    @Transactional
    public void delete(Long id) {
        Optional.ofNullable(clazzMapper.findById(id))
                .orElseThrow(() -> new EntityNotFoundException("班级不存在: " + id));
        long studentCount = studentMapper.countByClassId(id);
        if (studentCount > 0) {
            throw new BusinessException(
                    "班级下存在 " + studentCount + " 名学生，无法删除",
                    HttpStatus.CONFLICT
            );
        }
        clazzMapper.deleteById(id);
    }
}
