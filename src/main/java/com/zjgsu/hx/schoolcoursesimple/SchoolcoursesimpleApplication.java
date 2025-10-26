package com.zjgsu.hx.schoolcoursesimple;

import com.zjgsu.hx.schoolcoursesimple.model.Course;
import com.zjgsu.hx.schoolcoursesimple.model.Instructor;
import com.zjgsu.hx.schoolcoursesimple.model.ScheduleSlot;
import com.zjgsu.hx.schoolcoursesimple.model.Student;
import com.zjgsu.hx.schoolcoursesimple.service.CourseService;
import com.zjgsu.hx.schoolcoursesimple.service.StudentService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchoolcoursesimpleApplication {
	/*@Autowired
	private StudentService studentService;
	private CourseService courseService;*/

	public static void main(String[] args) {
		SpringApplication.run(SchoolcoursesimpleApplication.class, args);
	}
	/*@PostConstruct
	public void init() {
		Student teststudent1 = new Student("S9999","TestStudent","TestMajor",2023,"aab@abb.com");
		Course testcourse1 = new Course("C9999","TestCourse",new Instructor("I9999","TestInstructor","aaa@bbb.com"),new ScheduleSlot("Mon","8:05","9:50",0),50);
		studentService.createStudent(teststudent1);
		courseService.createCourse(testcourse1);
	}*/

}
