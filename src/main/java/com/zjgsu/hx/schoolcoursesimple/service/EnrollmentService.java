package com.zjgsu.hx.schoolcoursesimple.service;

import com.zjgsu.hx.schoolcoursesimple.exception.ResourceConflictException;
import com.zjgsu.hx.schoolcoursesimple.exception.ResourceNotFoundException;
import com.zjgsu.hx.schoolcoursesimple.model.Course;
import com.zjgsu.hx.schoolcoursesimple.model.Enrollment;
import com.zjgsu.hx.schoolcoursesimple.model.Student;
import com.zjgsu.hx.schoolcoursesimple.repository.CourseRepository;
import com.zjgsu.hx.schoolcoursesimple.repository.EnrollmentRepository;
import com.zjgsu.hx.schoolcoursesimple.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService; // 为了更新已选人数

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository,
                             CourseService courseService) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.courseService = courseService;
    }
    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }
    /**
     * 创建选课记录（选课）
     */
    public Enrollment createEnrollment(String studentId, String courseId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("学生不存在"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("课程不存在"));
        boolean alreadyEnrolled = enrollmentRepository.findByStudentId(studentId).stream()
                .anyMatch(e -> e.getCourseId().equals(courseId));
        if (alreadyEnrolled) {
            throw new IllegalArgumentException("该学生已选过此课程");
        }
        if (course.getEnrolled() >= course.getCapacity()) {
            throw new ResourceConflictException("选课失败：课程容量已满");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);

        enrollmentRepository.save(enrollment);
        courseService.increaseEnrollmentCount(courseId);

        return enrollment;
    }
    public List<Enrollment> getEnrollmentsByStudentId(String studentId) {
        // 确保学生存在
        studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("学生不存在"));
        return enrollmentRepository.findByStudentId(studentId);
    }
    public List<Enrollment> getEnrollmentsByCourseId(String courseId) {
        // 确保课程存在
        courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("课程不存在"));
        return enrollmentRepository.findByCourseId(courseId);
    }
    /*
     *删除选课记录（退课）
     */
    public Enrollment deleteEnrollment(String studentId, String courseId) {
        // 找到该学生的选课记录
        Enrollment enrollment = enrollmentRepository.findByStudentId(studentId).stream()
                .filter(e -> e.getCourseId().equals(courseId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("选课记录不存在!"));
        // 删除选课记录
        enrollmentRepository.deleteById(enrollment.getId());
        // 更新课程人数
        courseService.decreaseEnrollmentCount(courseId);
        return enrollment;
    }
    //删除选课记录（byId)
    public Enrollment deleteById(String id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("选课记录不存在!"));
        enrollmentRepository.deleteById(id);
        courseService.decreaseEnrollmentCount(enrollment.getCourseId());
        return enrollment;
    }
}
