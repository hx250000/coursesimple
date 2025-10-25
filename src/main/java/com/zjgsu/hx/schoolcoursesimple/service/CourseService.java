package com.zjgsu.hx.schoolcoursesimple.service;

import com.zjgsu.hx.schoolcoursesimple.exception.ResourceConflictException;
import com.zjgsu.hx.schoolcoursesimple.exception.ResourceNotFoundException;
import com.zjgsu.hx.schoolcoursesimple.model.Course;
import com.zjgsu.hx.schoolcoursesimple.repository.CourseRepository;
import com.zjgsu.hx.schoolcoursesimple.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    public CourseService(CourseRepository courseRepository,
                         EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }
    public List<Course> findAll() {
        return courseRepository.findAll();
    }
    public Course findById(String id) {
        return courseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("课程不存在！"));
    }
    public Course createCourse(Course course) {
        if (course.getCourseId() == null || course.getTitle() == null) {
            throw new IllegalArgumentException("课程代码和名称不能为空！");
        }
        boolean exists = courseRepository.findAll().stream()
                .anyMatch(c -> c.getCourseId().equals(course.getCourseId()));
        if (exists) {
            throw new IllegalArgumentException("课程代码已存在！");
        }
        return courseRepository.save(course);
    }
    public Course updateCourse(Course course) {
        Course existing = courseRepository.findById(course.getId())
                .orElseThrow(() -> new ResourceNotFoundException("课程不存在！"));
        if (course.getCourseId() == null || course.getTitle() == null) {
            throw new IllegalArgumentException("课程代码和名称不能为空！");
        }
        existing.setCourseId(course.getCourseId());
        existing.setTitle(course.getTitle());
        existing.setInstructor(course.getInstructor());
        existing.setCapacity(course.getCapacity());
        existing.setScheduleSlot(course.getScheduleSlot());
        return courseRepository.save(existing);
    }
    public Course deleteById(String id) {
        Course course = courseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("课程不存在！"));
        boolean hasEnrollments = !enrollmentRepository
                .findByCourseId(course.getId())
                .isEmpty();
        if (hasEnrollments) {
            throw new ResourceConflictException("无法删除：该课程存在选课记录！");
        }
        return courseRepository.deleteById(id);
    }
    public int increaseEnrollmentCount(String courseId) {
        Course course = this.findById(courseId);
        if (course.getEnrolled()>=course.getCapacity()){
            throw new ResourceConflictException("选课人数已满！");
        }
        return courseRepository.increaseEnrollmentCount(courseId);
    }
    public int decreaseEnrollmentCount(String courseId) {
        Course course = this.findById(courseId);
        if (course.getEnrolled()<=0){
            throw new ResourceConflictException("选课人数已空！");
        }
        return courseRepository.decreaseEnrollmentCount(courseId);
    }
}
