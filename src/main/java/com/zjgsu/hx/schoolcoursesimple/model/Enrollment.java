package com.zjgsu.hx.schoolcoursesimple.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Enrollment {
    private String id;
    private String studentId;
    private String courseId;
    private LocalDateTime createdAt;

    public Enrollment() {
        this.setId();
        this.setCreatedAt();
    }

    public Enrollment(String studentId, String courseId) {
        this.setId();
        this.setCreatedAt();
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public String getId() { return id; }
    public void setId() { this.id = UUID.randomUUID().toString(); }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt() { this.createdAt = LocalDateTime.now(); }


}
