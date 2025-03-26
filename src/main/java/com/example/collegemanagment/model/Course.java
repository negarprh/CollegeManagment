package com.example.collegemanagment.model;


public class Course {
    private String code;
    private String title;
    private int credits;
    private String instructor;

    public Course(String code, String title, int credits, String instructor) {
        this.code = code;
        this.title = title;
        this.credits = credits;
        this.instructor = instructor;
    }

    // Getters for our properties
    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public int getCredits() {
        return credits;
    }

    public String getInstructor() {
        return instructor;
    }

    @Override
    public String toString() {
        return code + ": " + title + " (" + credits + " credits, taught by " + instructor + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return code.equals(course.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}