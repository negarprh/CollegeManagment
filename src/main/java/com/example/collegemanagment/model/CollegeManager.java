package com.example.collegemanagment.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollegeManager {
    private List<Student> students;
    private List<Course> courses;
    private int nextStudentId;

    public CollegeManager() {
        students = new ArrayList<>();
        courses = new ArrayList<>();
        nextStudentId = 1000; // Start student IDs at 1000
    }

    // Method to add a new student
    public Student addStudent(String name, String email) {
        Student student = new Student(nextStudentId++, name, email);
        students.add(student);
        return student;
    }

    // Method to add a new course
    public Course addCourse(String code, String title, int credits, String instructor) {
        Course course = new Course(code, title, credits, instructor);
        courses.add(course);
        return course;
    }

    // Method to register a student for a course
    public boolean registerStudentForCourse(int studentId, String courseCode) {
        Student student = findStudentById(studentId);
        Course course = findCourseByCode(courseCode);

        if (student != null && course != null) {
            student.registerForCourse(course);
            return true;
        }
        return false;
    }

    // Helper method to find a student by ID
    public Student findStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    // Helper method to find a course by code
    public Course findCourseByCode(String code) {
        for (Course course : courses) {
            if (course.getCode().equals(code)) {
                return course;
            }
        }
        return null;
    }

    // Methods to get all students and courses
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }

    public Map<String, Integer> getCoursesRegistrationStats() {
        Map<String, Integer> stats = new HashMap<>();

        for (Course course : courses) {
            int count = 0;
            for (Student student : students) {
                if (student.isRegisteredFor(course)) {
                    count++;
                }
            }
            stats.put(course.getCode(), count);
        }

        return stats;
    }
}
