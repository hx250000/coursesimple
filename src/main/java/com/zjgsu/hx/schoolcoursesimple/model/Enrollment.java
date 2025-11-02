package com.zjgsu.hx.schoolcoursesimple.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="enrollment",uniqueConstraints = {@UniqueConstraint(columnNames = {"course_id", "student_id"})})
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /*private String studentId;
    private String courseId;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;//关联学生

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;   // 关联课程

    @Enumerated(EnumType.STRING)
    private Status status;   // 选课状态（ACTIVE, DROPPED, COMPLETED）

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = Status.ACTIVE;
    }

    public Enrollment() {

    }

    public Enrollment(Student student, Course course) {
        this.course=course;
        this.student=student;

        /*this.studentId = studentId;
        this.courseId = courseId;*/
    }

    public String getId() { return id; }
    public void setId() { this.id = UUID.randomUUID().toString(); }
    /*public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }*/
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt() { this.createdAt = LocalDateTime.now(); }
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}


}
