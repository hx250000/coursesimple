package com.zjgsu.hx.schoolcoursesimple.repository;

import com.zjgsu.hx.schoolcoursesimple.model.Student;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class StudentRepository {
    private final Map<String, Student> students = new ConcurrentHashMap<String, Student>();

    public List<Student> findAll() {
        return new ArrayList<Student>(students.values());
    }

    public Optional<Student> findById(String id) {
        return Optional.ofNullable(students.get(id));
    }

    public Optional<Student> findByStudentId(String studentId) {
        return students.values().stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst();
    }

    public Student save(Student student) {
        students.put(student.getId(), student);
        return student;
    }

    public Student deleteById(String id) {
        return students.remove(id);
    }

    public Student deleteByStudentId(String studentId) {
        Optional<Student> stuToDelete = this.findByStudentId(studentId);
        if (stuToDelete.isPresent()) {
            students.remove(stuToDelete.get().getStudentId());
            return stuToDelete.get();
        }
        return null;
    }
}
