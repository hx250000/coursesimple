package com.zjgsu.hx.schoolcoursesimple.repository;

import com.zjgsu.hx.schoolcoursesimple.model.Course;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CourseRepository {
    private final Map<String, Course> courses = new ConcurrentHashMap<String, Course>();

    public List<Course> findAll() {
        return new ArrayList<Course>(courses.values());
    }

    public Optional<Course> findById(String id) {
        return Optional.ofNullable(courses.get(id));
    }

    public Course save(Course course) {
        return courses.put(course.getId(), course);
    }

    public Course deleteById(String id) {
        return courses.remove(id);
    }

    public int increaseEnrollmentCount(String id){
        Course course = courses.get(id);
        course.addEnrolled();
        return course.getEnrolled();
    }

    public int decreaseEnrollmentCount(String id){
        Course course = courses.get(id);
        course.deleteEnrolled();
        return course.getEnrolled();
    }
}
