package com.example.collegemanagment.model;


public class Main {
    public static void main(String[] args) {
        // Create our college manager
        CollegeManager manager = new CollegeManager();

        // Add some students
        Student alice = manager.addStudent("Alice Smith", "alice@example.com");
        Student bob = manager.addStudent("Bob Johnson", "bob@example.com");

        // Add some courses
        Course javaCourse = manager.addCourse("CS101", "Introduction to Java", 3, "Dr. Java");
        Course webDev = manager.addCourse("CS201", "Web Development", 4, "Prof. Web");

        // Register students for courses
        manager.registerStudentForCourse(alice.getId(), javaCourse.getCode());
        manager.registerStudentForCourse(alice.getId(), webDev.getCode());
        manager.registerStudentForCourse(bob.getId(), javaCourse.getCode());

        // Print out student registrations
        System.out.println("Alice's courses:");
        for (Course course : alice.getRegisteredCourses()) {
            System.out.println("- " + course);
        }

        System.out.println("\nBob's courses:");
        for (Course course : bob.getRegisteredCourses()) {
            System.out.println("- " + course);
        }
    }
}

// Model code (sealed types, i.e. no `public` access modifier)


