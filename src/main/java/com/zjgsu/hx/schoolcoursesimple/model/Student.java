package com.zjgsu.hx.schoolcoursesimple.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Student {
    private String id;
    private String studentId;
    private String name;
    private String major;
    private int grade;
    private String email;
    private LocalDateTime createAt;

    public Student() {
        this.setId();
        this.setCreateAt();
    }

    public Student(String studentId, String name, String major, int grade, String email) {
        this.setId();
        this.setCreateAt();
        this.studentId = studentId;
        this.name = name;
        this.major = major;
        this.grade = grade;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt() {
        this.createAt = LocalDateTime.now();
    }
}
