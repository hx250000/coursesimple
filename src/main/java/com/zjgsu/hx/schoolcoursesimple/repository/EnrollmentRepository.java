package com.zjgsu.hx.schoolcoursesimple.repository;

import com.zjgsu.hx.schoolcoursesimple.model.Enrollment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EnrollmentRepository {
    private final Map<String, Enrollment> enrollments=new ConcurrentHashMap<String,Enrollment>();

    public List<Enrollment> findAll(){
        return new ArrayList<Enrollment>(enrollments.values());
    }

    public void save(Enrollment enrollment) {
        enrollments.put(enrollment.getId(), enrollment);
    }

    public Optional<Enrollment> findById(String id) {
        return Optional.ofNullable(enrollments.get(id));
    }

    public List<Enrollment> findByStudentId(String studentId) {
        return enrollments.values().stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<Enrollment> findByCourseId(String courseId) {
        return enrollments.values().stream()
                .filter(e -> e.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }

    public Optional<Enrollment> findByStudentAndCourse(String studentId, String courseId) {
        return enrollments.values().stream()
                .filter(e -> e.getStudentId().equals(studentId) && e.getCourseId().equals(courseId))
                .findFirst();
    }

    public void deleteById(String id) {
        enrollments.remove(id);
    }
}
