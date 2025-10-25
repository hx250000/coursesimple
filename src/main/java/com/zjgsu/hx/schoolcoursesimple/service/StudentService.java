package com.zjgsu.hx.schoolcoursesimple.service;

import com.zjgsu.hx.schoolcoursesimple.exception.ResourceConflictException;
import com.zjgsu.hx.schoolcoursesimple.exception.ResourceNotFoundException;
import com.zjgsu.hx.schoolcoursesimple.model.Student;
import com.zjgsu.hx.schoolcoursesimple.repository.EnrollmentRepository;
import com.zjgsu.hx.schoolcoursesimple.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    public StudentService(StudentRepository studentRepository, EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }
    public List<Student> findAll() {
        return studentRepository.findAll();
    }
    public Student findById(String id) {
        return studentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("学生不存在！"));
    }
    public Student findByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId).orElseThrow(()->new ResourceNotFoundException("学生不存在！"));
    }
    public Student createStudent(Student student) {
        String studentId = student.getStudentId();
        String email = student.getEmail();
        String emailregex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        boolean isemailcorrect = email.matches(emailregex);
        if (!isemailcorrect) {
            throw new IllegalArgumentException("邮箱格式不正确！");
        }
        boolean exists = studentRepository.findByStudentId(studentId).isPresent();
        if (exists) {
            throw new ResourceConflictException("该学号已存在！");
        }
        return studentRepository.save(student);
    }
    public Student updateStudent(Student student) {
        // 检查是否存在
        Student existing = studentRepository.findById(student.getId()).orElseThrow(()->new ResourceNotFoundException("学生不存在！"));
        // 保留创建时间
        //student.setCreateAt(existing.getCreateAt());
        // 校验邮箱
        String email = student.getEmail();
        String emailregex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(emailregex)) {
            throw new IllegalArgumentException("邮箱格式不正确！");
        }
        // 检查学号是否与其他学生重复
        studentRepository.findByStudentId(student.getStudentId())
                .filter(s -> !s.getId().equals(student.getId()))
                .ifPresent(s -> {
                    throw new ResourceConflictException("学号已存在！");
                });
        // 保存更新
        existing.setStudentId(student.getStudentId());
        existing.setName(student.getName());
        existing.setEmail(student.getEmail());
        existing.setMajor(student.getMajor());
        return studentRepository.save(existing);
    }

    public Student deleteById(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("学生不存在!"));
        boolean hasEnrollments = !enrollmentRepository
                .findByStudentId(student.getStudentId())
                .isEmpty();
        if (hasEnrollments) {
            throw new ResourceConflictException("无法删除：该学生存在选课记录！");
        }
        return studentRepository.deleteById(id);
    }
}
