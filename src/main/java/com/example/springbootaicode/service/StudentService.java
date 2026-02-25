package com.example.springbootaicode.service;

import com.example.springbootaicode.dto.*;
import com.example.springbootaicode.exception.BusinessException;
import com.example.springbootaicode.exception.EntityNotFoundException;
import com.example.springbootaicode.mapper.ClazzMapper;
import com.example.springbootaicode.mapper.StudentMapper;
import com.example.springbootaicode.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {

    private final StudentMapper studentMapper;
    private final ClazzMapper clazzMapper;

    public PageResponse<StudentResponse> list(String name, String studentNo, Long classId, PageQuery pageQuery) {
        List<Student> students = studentMapper.findByCondition(name, studentNo, classId, pageQuery.offset(), pageQuery.size());
        long total = studentMapper.countByCondition(name, studentNo, classId);
        List<StudentResponse> content = students.stream().map(StudentResponse::from).toList();
        return PageResponse.of(content, pageQuery.page(), pageQuery.size(), total);
    }

    public StudentResponse getById(Long id) {
        return Optional.ofNullable(studentMapper.findById(id))
                .map(StudentResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("学生不存在: " + id));
    }

    @Transactional
    public StudentResponse create(CreateStudentRequest request) {
        if (studentMapper.existsByStudentNo(request.studentNo(), null)) {
            throw new BusinessException("学号已存在: " + request.studentNo(), HttpStatus.CONFLICT);
        }
        if (request.classId() != null && clazzMapper.findById(request.classId()) == null) {
            throw new EntityNotFoundException("班级不存在: " + request.classId());
        }
        Student student = Student.builder()
                .studentNo(request.studentNo())
                .name(request.name())
                .gender(request.gender())
                .age(request.age())
                .phone(request.phone())
                .email(request.email())
                .classId(request.classId())
                .build();
        studentMapper.insert(student);
        return StudentResponse.from(student);
    }

    @Transactional
    public StudentResponse update(Long id, UpdateStudentRequest request) {
        Student existing = Optional.ofNullable(studentMapper.findById(id))
                .orElseThrow(() -> new EntityNotFoundException("学生不存在: " + id));
        if (request.studentNo() != null && studentMapper.existsByStudentNo(request.studentNo(), id)) {
            throw new BusinessException("学号已存在: " + request.studentNo(), HttpStatus.CONFLICT);
        }
        if (request.classId() != null && clazzMapper.findById(request.classId()) == null) {
            throw new EntityNotFoundException("班级不存在: " + request.classId());
        }
        existing.setStudentNo(request.studentNo() != null ? request.studentNo() : existing.getStudentNo());
        existing.setName(request.name() != null ? request.name() : existing.getName());
        existing.setGender(request.gender() != null ? request.gender() : existing.getGender());
        existing.setAge(request.age() != null ? request.age() : existing.getAge());
        existing.setPhone(request.phone() != null ? request.phone() : existing.getPhone());
        existing.setEmail(request.email() != null ? request.email() : existing.getEmail());
        existing.setClassId(request.classId() != null ? request.classId() : existing.getClassId());
        studentMapper.update(existing);
        return StudentResponse.from(existing);
    }

    @Transactional
    public void delete(Long id) {
        Optional.ofNullable(studentMapper.findById(id))
                .orElseThrow(() -> new EntityNotFoundException("学生不存在: " + id));
        studentMapper.deleteById(id);
    }
}
