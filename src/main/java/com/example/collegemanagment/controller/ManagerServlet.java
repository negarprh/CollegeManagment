package com.example.collegemanagment.controller;

import com.example.collegemanagment.model.CollegeManager;
import com.example.collegemanagment.model.Course;
import com.example.collegemanagment.model.Student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/manager")
public class ManagerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the session and college manager
        HttpSession session = request.getSession();
        CollegeManager manager = getCollegeManager(session);

        // Get the action parameter to determine what operation to perform
        String action = request.getParameter("action");

        if ("addStudent".equals(action)) {
            addStudent(request, manager, session);
        } else if ("addCourse".equals(action)) {
            addCourse(request, manager, session);
        } else if ("registerStudentForCourse".equals(action)) {
            registerStudentForCourse(request, manager, session);
        } else if ("dropCourse".equals(action)) {
            dropCourse(request, manager, session);
        }

        // Redirect back to the manager page to see the updated data
        response.sendRedirect(request.getContextPath() + "/manager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        CollegeManager manager = getCollegeManager(session);

        // Check if we're viewing a specific course
        String courseCode = request.getParameter("courseCode");
        if (courseCode != null && !courseCode.isEmpty()) {
            Course course = manager.findCourseByCode(courseCode);
            if (course != null) {
                request.setAttribute("course", course);

                // Find all students registered for this course
                List<Student> enrolledStudents = new ArrayList<>();
                for (Student student : manager.getAllStudents()) {
                    if (student.isRegisteredFor(course)) {
                        enrolledStudents.add(student);
                    }
                }
                request.setAttribute("enrolledStudents", enrolledStudents);

                request.getRequestDispatcher("/WEB-INF/views/courseDetails.jsp").forward(request, response);
                return;
            }
        }

        // Handle student search functionality
        String action = request.getParameter("action");
        if ("searchStudents".equals(action)) {
            String query = request.getParameter("q");
            if (query != null && !query.trim().isEmpty()) {
                List<Student> searchResults = new ArrayList<>();

                try {
                    int studentId = Integer.parseInt(query);
                    Student student = manager.findStudentById(studentId);
                    if (student != null) {
                        searchResults.add(student);
                    }
                } catch (NumberFormatException e) {
                    // Not a numeric ID, search by name
                    for (Student student : manager.getAllStudents()) {
                        if (student.getName().toLowerCase().contains(query.toLowerCase())) {
                            searchResults.add(student);
                        }
                    }
                }

                request.setAttribute("searchResults", searchResults);
                request.setAttribute("searchQuery", query);
            }
        }

        request.setAttribute("collegeManager", manager);
        request.getRequestDispatcher("/WEB-INF/views/manager.jsp").forward(request, response);
    }

    /**
     * Retrieves or initializes the CollegeManager instance from session.
     */
    private CollegeManager getCollegeManager(HttpSession session) {
        CollegeManager manager = (CollegeManager) session.getAttribute("collegeManager");
        if (manager == null) {
            manager = new CollegeManager();
            session.setAttribute("collegeManager", manager);
        }
        return manager;
    }

    /**
     * Handles adding a new student with validation and error handling.
     */
    private void addStudent(HttpServletRequest request, CollegeManager manager, HttpSession session) {
        String name = request.getParameter("studentName");
        String email = request.getParameter("studentEmail");

        if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            session.setAttribute("error", "Please fill in all required fields.");
            return;
        }

        try {
            Student student = manager.addStudent(name, email);
            session.setAttribute("message", "Student " + student.getName() + " added successfully!");
        } catch (Exception e) {
            session.setAttribute("error", "Failed to add student: " + e.getMessage());
        }
    }

    /**
     * Handles adding a new course with validation and error handling.
     */
    private void addCourse(HttpServletRequest request, CollegeManager manager, HttpSession session) {
        String code = request.getParameter("courseCode");
        String title = request.getParameter("courseTitle");
        String creditsStr = request.getParameter("courseCredits");
        String instructor = request.getParameter("courseInstructor");

        if (code == null || code.trim().isEmpty() ||
                title == null || title.trim().isEmpty() ||
                creditsStr == null || creditsStr.trim().isEmpty() ||
                instructor == null || instructor.trim().isEmpty()) {
            session.setAttribute("error", "Please fill in all fields to add a course.");
            return;
        }

        try {
            int credits = Integer.parseInt(creditsStr);
            manager.addCourse(code, title, credits, instructor);
            session.setAttribute("message", "Course " + title + " added successfully!");
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Credits must be a valid number.");
        } catch (Exception e) {
            session.setAttribute("error", "Failed to add course: " + e.getMessage());
        }
    }

    /**
     * Handles student registration for a course with validation.
     */
    private void registerStudentForCourse(HttpServletRequest request, CollegeManager manager, HttpSession session) {
        String studentIdStr = request.getParameter("studentId");
        String courseCode = request.getParameter("courseCode");

        if (studentIdStr == null || studentIdStr.trim().isEmpty() ||
                courseCode == null || courseCode.trim().isEmpty()) {
            session.setAttribute("error", "Please select both a student and a course.");
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdStr);
            Student student = manager.findStudentById(studentId);
            Course course = manager.findCourseByCode(courseCode);

            if (student != null && course != null) {
                if (student.isRegisteredFor(course)) {
                    session.setAttribute("error", "Student " + student.getName() + " is already registered for " + course.getCode() + "!");
                } else {
                    manager.registerStudentForCourse(studentId, courseCode);
                    session.setAttribute("message", "Student " + student.getName() + " registered for " + course.getCode() + " successfully!");
                }
            }
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid student ID format!");
        }
    }

    /**
     * Handles dropping a course for a student.
     */
    private void dropCourse(HttpServletRequest request, CollegeManager manager, HttpSession session) {
        String studentIdStr = request.getParameter("studentId");
        String courseCode = request.getParameter("courseCode");

        if (studentIdStr == null || courseCode == null) {
            session.setAttribute("error", "Please select a valid student and course.");
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdStr);
            Student student = manager.findStudentById(studentId);
            Course course = manager.findCourseByCode(courseCode);

            if (student != null && course != null && student.isRegisteredFor(course)) {
                student.dropCourse(course);
                session.setAttribute("message", "Student " + student.getName() + " dropped " + course.getCode() + " successfully!");
            } else {
                session.setAttribute("error", "Student is not registered for this course!");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid student ID format!");
        }
    }
}
