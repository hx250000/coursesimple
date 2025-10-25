package com.zjgsu.hx.schoolcoursesimple.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Course {
    private String id; //uuid
    private String courseId; //课程id
    private String title; //课程名
    private Instructor instructor;
    private ScheduleSlot scheduleSlot;
    private int capacity;
    private int enrolled;
    private LocalDateTime createdAt;

    public Course() {
        this.setId();
        this.setCreatedAt();
    }

    public Course(String courseId, String title, Instructor instructor, ScheduleSlot scheduleSlot, int capacity) {
        this.setId();
        this.setCreatedAt();
        this.courseId = courseId;
        this.title = title;
        this.instructor = instructor;
        this.scheduleSlot = scheduleSlot;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public ScheduleSlot getScheduleSlot() {
        return scheduleSlot;
    }

    public void setScheduleSlot(ScheduleSlot scheduleSlot) {
        this.scheduleSlot = scheduleSlot;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(int enrolled) {
        this.enrolled = enrolled;
    }

    public void addEnrolled(){
        this.enrolled=this.enrolled+1;
    }

    public void deleteEnrolled(){
        this.enrolled=this.enrolled-1;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

}
