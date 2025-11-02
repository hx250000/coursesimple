package com.zjgsu.hx.schoolcoursesimple.service;

import com.zjgsu.hx.schoolcoursesimple.exception.ResourceConflictException;
import com.zjgsu.hx.schoolcoursesimple.exception.ResourceNotFoundException;
import com.zjgsu.hx.schoolcoursesimple.model.Course;
import com.zjgsu.hx.schoolcoursesimple.model.ScheduleSlot;
import com.zjgsu.hx.schoolcoursesimple.repository.CourseRepository;
import com.zjgsu.hx.schoolcoursesimple.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
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
        return courseRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("课程不存在！"));
    }
    public Course createCourse(Course course) {
        validateCourse(course, true);
        if (course.getCourseId() == null || course.getTitle() == null) {
            throw new IllegalArgumentException("课程代码和名称不能为空！");
        }
        boolean exists=courseRepository.existsById(course.getCourseId());
        if (exists) {
            throw new IllegalArgumentException("课程代码已存在！");
        }
        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourse(String id,Course course) {

        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("课程不存在！"));
        validateCourse(course, false);
        if (course.getCourseId() == null || course.getTitle() == null) {
            throw new IllegalArgumentException("课程代码和名称不能为空！");
        }

        ScheduleSlot slot=existing.getScheduleSlot();

        existing.setCourseId(course.getCourseId());
        existing.setTitle(course.getTitle());
        existing.setInstructor(course.getInstructor());
        existing.setCapacity(course.getCapacity());

        //existing.setScheduleSlot(course.getScheduleSlot());

        slot.setDayOfWeek(course.getScheduleSlot().getDayOfWeek());
        slot.setEndTime(course.getScheduleSlot().getEndTime());
        slot.setStartTime(course.getScheduleSlot().getStartTime());
        slot.setExpectedAttendance(course.getScheduleSlot().getExpectedAttendance());
        existing.setScheduleSlot(slot);

        return courseRepository.save(existing);
    }

    @Transactional
    public Course deleteById(String id) {
        Course course = courseRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("课程不存在！"));
        boolean hasEnrollments = !enrollmentRepository
                .findByCourse(course)
                .isEmpty();
        if (hasEnrollments) {
            throw new ResourceConflictException("无法删除：该课程存在选课记录！");
        }
        courseRepository.deleteById(id);
        return course;
    }

    @Transactional
    public int increaseEnrollmentCount(String courseId) {
        Course course = courseRepository.findByCourseId(courseId).
                orElseThrow(()->new ResourceNotFoundException("课程不存在！"));
        if (course.getEnrolled()>=course.getCapacity()){
            throw new ResourceConflictException("选课人数已满！");
        }
        course.addEnrolled();
        courseRepository.save(course);
        return course.getEnrolled();
    }

    @Transactional
    public int decreaseEnrollmentCount(String courseId) {
        Course course = courseRepository.findByCourseId(courseId).
                orElseThrow(()->new ResourceNotFoundException("课程不存在！"));
        if (course.getEnrolled()<=0){
            throw new ResourceConflictException("选课人数已空！");
        }
        course.deleteEnrolled();
        courseRepository.save(course);
        return course.getEnrolled();
    }

    private void validateCourse(Course course, boolean isCreating) {
        if (course == null) {
            throw new IllegalArgumentException("课程信息不能为空！");
        }
        if (course.getCourseId() == null || course.getCourseId().trim().isEmpty()) {
            throw new IllegalArgumentException("课程代码不能为空！");
        }
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("课程名称不能为空！");
        }
        if (course.getInstructor() == null) {
            throw new IllegalArgumentException("授课教师不能为空！");
        }
        if (course.getScheduleSlot() == null) {
            throw new IllegalArgumentException("课程时间安排不能为空！");
        }
        if (course.getCapacity() <= 0) {
            throw new IllegalArgumentException("课程容量必须大于 0！");
        }
    }
}
