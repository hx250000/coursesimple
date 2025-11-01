package com.zjgsu.hx.schoolcoursesimple.controller;

import com.zjgsu.hx.schoolcoursesimple.model.Course;
import com.zjgsu.hx.schoolcoursesimple.service.CourseService;
import com.zjgsu.hx.schoolcoursesimple.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ApiResponse<List<Course>> getAllCourses() {
        return ApiResponse.success(courseService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Course> getCourseById(@PathVariable String id) {
        return ApiResponse.success(courseService.findById(id));
    }

    @PostMapping
    public ApiResponse<Course> createCourse(@RequestBody Course course) {
        return ApiResponse.success(courseService.createCourse(course));
    }

    @PutMapping("/{id}")
    public ApiResponse<Course> updateCourse(@PathVariable String id, @RequestBody Course course) {
        return ApiResponse.success(courseService.updateCourse(id,course));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Course> deleteCourse(@PathVariable String id) {
        return ApiResponse.success(courseService.deleteById(id));
    }
}
