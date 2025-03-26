package com.example.collegemanagment.model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private int id;
    private String name;
    private String email;
    private List<Course> registeredCourses;

    public Student(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.registeredCourses = new ArrayList<>();
    }

    // Getters for our properties
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Course> getRegisteredCourses() {
        return registeredCourses;
    }

    public void registerForCourse(Course course) {
        if (!registeredCourses.contains(course)) {
            registeredCourses.add(course);
        }
    }

    public void dropCourse(Course course) {
        registeredCourses.remove(course);
    }

    public boolean isRegisteredFor(Course course) {
        return registeredCourses.contains(course);
    }

    @Override
    public String toString() {
        return "Student #" + id + ": " + name + " (" + email + ")";
    }
}
