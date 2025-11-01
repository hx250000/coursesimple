package com.zjgsu.hx.schoolcoursesimple.repository;

import com.zjgsu.hx.schoolcoursesimple.model.Enrollment;
import com.zjgsu.hx.schoolcoursesimple.model.Student;
import com.zjgsu.hx.schoolcoursesimple.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {

    /**
     * 按学生查询选课记录
     */
    List<Enrollment> findByStudent(Student student);

    /**
     * 按课程查询选课记录
     */
    List<Enrollment> findByCourse(Course course);

    /**
     * 按学生和课程联合查询（判断是否重复选课）
     */
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);

    /**
     * 判断是否存在该选课关系
     */
    boolean existsByStudentAndCourse(Student student, Course course);

    /**
     * 删除某学生与课程的选课记录
     */
    void deleteByStudentAndCourse(Student student, Course course);
}
