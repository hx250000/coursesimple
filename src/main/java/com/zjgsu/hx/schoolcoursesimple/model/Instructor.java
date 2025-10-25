package com.zjgsu.hx.schoolcoursesimple.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Instructor {
    private String id; //uuid
    private String instructorId; //教师id
    private String name; //
    private String email;
    private LocalDateTime createAt;

    public Instructor() {
        this.setId();
        this.setCreateAt();
    }

    public Instructor(String instructorId, String name, String email) {
        this.setId();
        this.setInstructorId(instructorId);
        this.setName(name);
        this.setEmail(email);
        this.setCreateAt();
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt() {
        this.createAt = LocalDateTime.now();
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
