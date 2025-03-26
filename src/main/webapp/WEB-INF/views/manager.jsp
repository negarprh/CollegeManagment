<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>College Manager</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<h1>🎓 College Manager System</h1>

<!-- Display Success and Error Messages -->
<c:if test="${not empty sessionScope.message}">
    <div class="message success">${sessionScope.message}</div>
    <c:remove var="message" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.error}">
    <div class="message error">${sessionScope.error}</div>
    <c:remove var="error" scope="session"/>
</c:if>

<div class="container">
    <!-- Add Course Form -->
    <div class="card">
        <h2>Add New Course 📚</h2>
        <form action="${pageContext.request.contextPath}/manager" method="post">
            <input type="hidden" name="action" value="addCourse">
            <label>Course Code: <input type="text" name="courseCode" required></label>
            <label>Course Title: <input type="text" name="courseTitle" required></label>
            <label>Credits: <input type="number" name="courseCredits" min="1" max="6" required></label>
            <label>Instructor: <input type="text" name="courseInstructor" required></label>
            <button type="submit">Add Course</button>
        </form>
    </div>

    <!-- Add Student Form -->
    <div class="card">
        <h2>Add New Student 👨‍🎓</h2>
        <form action="${pageContext.request.contextPath}/manager" method="post">
            <input type="hidden" name="action" value="addStudent">
            <label>Student Name: <input type="text" name="studentName" required></label>
            <label>Email: <input type="email" name="studentEmail" required></label>
            <button type="submit">Add Student</button>
        </form>
    </div>

    <!-- Register Student for Course -->
    <div class="card">
        <h2>Register Student for Course 📝</h2>
        <form action="${pageContext.request.contextPath}/manager" method="post">
            <input type="hidden" name="action" value="registerStudentForCourse">
            <label>Select Student:
                <select name="studentId" required>
                    <option value="">-- Select Student --</option>
                    <c:forEach var="student" items="${collegeManager.allStudents}">
                        <option value="${student.id}">${student.name} (ID: ${student.id})</option>
                    </c:forEach>
                </select>
            </label>
            <label>Select Course:
                <select name="courseCode" required>
                    <option value="">-- Select Course --</option>
                    <c:forEach var="course" items="${collegeManager.allCourses}">
                        <option value="${course.code}">${course.code}: ${course.title}</option>
                    </c:forEach>
                </select>
            </label>
            <button type="submit">Register</button>
        </form>
    </div>
</div>

<!-- Students Table -->
<h2>Enrolled Students 👨‍🎓👩‍🎓</h2>
<c:choose>
    <c:when test="${empty collegeManager.allStudents}">
        <p>No students enrolled yet.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr><th>ID</th><th>Name</th><th>Email</th><th>Registered Courses</th></tr>
            </thead>
            <tbody>
            <c:forEach var="student" items="${collegeManager.allStudents}">
                <tr>
                    <td>${student.id}</td>
                    <td>${student.name}</td>
                    <td>${student.email}</td>
                    <td>
                        <c:forEach var="course" items="${student.registeredCourses}" varStatus="status">
                            ${course.code}${!status.last ? ', ' : ''}
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<!-- Courses Table -->
<h2>Available Courses 📚</h2>
<c:choose>
    <c:when test="${empty collegeManager.allCourses}">
        <p>No courses available yet.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr><th>Code</th><th>Title</th><th>Credits</th><th>Instructor</th><th>Enrollment</th></tr>
            </thead>
            <tbody>
            <c:forEach var="course" items="${collegeManager.allCourses}">
                <c:set var="enrollmentStats" value="${collegeManager.coursesRegistrationStats}" />
                <tr>
                    <td>${course.code}</td>
                    <td>${course.title}</td>
                    <td>${course.credits}</td>
                    <td>${course.instructor}</td>
                    <td>${enrollmentStats[course.code]} students</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<!-- Drop Course Form -->
<div class="card">
    <h2>Drop a Course ❌</h2>
    <form action="${pageContext.request.contextPath}/manager" method="post">
        <input type="hidden" name="action" value="dropCourse">
        <label>Select Student:
            <select name="studentId" required>
                <option value="">-- Select Student --</option>
                <c:forEach var="student" items="${collegeManager.allStudents}">
                    <option value="${student.id}">${student.name} (ID: ${student.id})</option>
                </c:forEach>
            </select>
        </label>
        <label>Select Course:
            <select name="courseCode" required>
                <option value="">-- Select Course --</option>
                <c:forEach var="course" items="${collegeManager.allCourses}">
                    <option value="${course.code}">${course.code}: ${course.title}</option>
                </c:forEach>
            </select>
        </label>
        <button type="submit">Drop Course</button>
    </form>
</div>

<!-- Search Students -->
<div class="card">
    <h2>Search Students 🔍</h2>
    <form action="${pageContext.request.contextPath}/manager" method="get">
        <input type="hidden" name="action" value="searchStudents">
        <label>Search by Name or ID: <input type="text" name="q" required></label>
        <button type="submit">Search</button>
    </form>
</div>

<!-- Display Search Results -->
<c:if test="${not empty searchResults}">
    <h2>Search Results for "${searchQuery}" 🔍</h2>
    <c:choose>
        <c:when test="${empty searchResults}">
            <p>No students found matching your search.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr><th>ID</th><th>Name</th><th>Email</th><th>Registered Courses</th></tr>
                </thead>
                <tbody>
                <c:forEach var="student" items="${searchResults}">
                    <tr>
                        <td>${student.id}</td>
                        <td>${student.name}</td>
                        <td>${student.email}</td>
                        <td>
                            <c:forEach var="course" items="${student.registeredCourses}" varStatus="status">
                                ${course.code}${!status.last ? ', ' : ''}
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</c:if>

</body>
</html>
