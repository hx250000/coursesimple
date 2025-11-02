package com.zjgsu.hx.schoolcoursesimple.service;

import com.zjgsu.hx.schoolcoursesimple.exception.ResourceConflictException;
import com.zjgsu.hx.schoolcoursesimple.exception.ResourceNotFoundException;
import com.zjgsu.hx.schoolcoursesimple.model.Course;
import com.zjgsu.hx.schoolcoursesimple.model.Enrollment;
import com.zjgsu.hx.schoolcoursesimple.model.Student;
import com.zjgsu.hx.schoolcoursesimple.repository.CourseRepository;
import com.zjgsu.hx.schoolcoursesimple.repository.EnrollmentRepository;
import com.zjgsu.hx.schoolcoursesimple.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    @Transactional
    public Enrollment createEnrollment(String studentId, String courseId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("学生不存在！"));

        Optional<Course> courseOpt = courseRepository.findByCourseId(courseId);
        System.out.println("正在查询课程 courseId=" + courseId + "，查询结果=" + courseOpt);
        Course course = courseOpt.orElseThrow(() -> new ResourceNotFoundException("课程不存在!"));
        /*Course course = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("课程不存在!"));*/
        boolean alreadyEnrolled = enrollmentRepository.existsByStudentAndCourse(student, course);
        if (alreadyEnrolled) {
            throw new IllegalArgumentException("该学生已选过此课程!");
        }
        if (course.getEnrolled() >= course.getCapacity()) {
            throw new ResourceConflictException("选课失败：课程容量已满!");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        enrollmentRepository.save(enrollment);
        courseService.increaseEnrollmentCount(courseId);

        return enrollment;
    }

    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        // 确保学生存在
        Student student=studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("学生不存在"));
        return enrollmentRepository.findByStudent(student);
    }
    public List<Enrollment> getEnrollmentsByCourse(String courseId) {
        // 确保课程存在
        Course course=courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("课程不存在"));
        return enrollmentRepository.findByCourse(course);
    }
    /*
     *删除选课记录（退课）
     */
    @Transactional
    public Enrollment deleteEnrollment(String studentId, String courseId) {
        // 找到该学生的选课记录
        System.out.printf("正在删除选课记录，学生=%s,课程=%s",studentId,courseId);
        Student student=studentRepository.findByStudentId(studentId)
                .orElseThrow(()->new ResourceNotFoundException("学生不存在！"));
        Course course=courseRepository.findByCourseId(courseId)
                .orElseThrow(()->new ResourceNotFoundException("课程不存在！"));

        Enrollment enrollment=enrollmentRepository.findByStudentAndCourse(student,course)
                .orElseThrow(()->new ResourceNotFoundException("选课记录不存在！"));

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
        courseService.decreaseEnrollmentCount(enrollment.getCourse().getId());
        return enrollment;
    }
}
