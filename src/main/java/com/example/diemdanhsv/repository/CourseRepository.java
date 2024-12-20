package com.example.diemdanhsv.repository;

import com.example.diemdanhsv.databaseConnect.DatabaseConnection;
import com.example.diemdanhsv.models.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository {

    public ObservableList<String> getCourses() {
        ObservableList<String> courses = FXCollections.observableArrayList();
        String query = "SELECT name FROM course"; // Lấy tên khóa học

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                courses.add(resultSet.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }

    // lấy danh sách môn học của sinh viên đăng nhập
    public List<Course> getCourseLogin(int studentId, int semester) {
        String courseIdQuery = "Select DISTINCT course_id From attendance Where student_id = ?";
        List<Course> courses = new ArrayList<>();

        // Lấy coureId từ student_coures
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement coursesIdStmt = conn.prepareStatement(courseIdQuery)) {
            coursesIdStmt.setInt(1, studentId);

            // Lấy couresName từ course
            try(ResultSet coursesIdRs = coursesIdStmt.executeQuery()) {
                while (coursesIdRs.next()) {
                    int courseId = coursesIdRs.getInt("course_id");
                    String coursesQurey = "Select name, start_date, end_date, day, academic_year From course Where id = ? And semester = ?";

                    try(PreparedStatement coursesStmt = conn.prepareStatement(coursesQurey)) {
                        coursesStmt.setInt(1, courseId);
                        coursesStmt.setInt(2, semester);

                        try (ResultSet courseRs = coursesStmt.executeQuery()) {
                            if (courseRs.next()) {
                                Course newCourse = new Course();
                                newCourse.setId(courseId);
                                newCourse.setName(courseRs.getString("name"));
                                newCourse.setStartDate(courseRs.getDate("start_date").toLocalDate());
                                newCourse.setEndDate(courseRs.getDate("end_date").toLocalDate());

                                courses.add(newCourse);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return courses;
    }

    public int getCourseIdByName(String courseName) {
        String query = "SELECT id FROM course WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id"); // Trả về ID của khóa học
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Trả về -1 nếu không tìm thấy
    }


    public void addCourseToDatabase(String nameCourse, LocalDate startDate, LocalDate endDate, String semesterValue) {
        // Câu truy vấn SQL
        String query = "INSERT INTO course (name, start_date, end_date, day, academic_year, semester) VALUES (?, ?, ?, ?, ?, ?)";

        // Lấy giá trị cho "day" và "academic_year"
        String dayValue = "Thứ " + (startDate.getDayOfWeek().getValue() + 1);
        String academicYear = startDate.getYear() + "-" + (startDate.getYear() + 1);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Gán giá trị cho các placeholder (?)
            stmt.setString(1, nameCourse);
            stmt.setDate(2, java.sql.Date.valueOf(startDate));
            stmt.setDate(3, java.sql.Date.valueOf(endDate));
            stmt.setString(4, dayValue);
            stmt.setString(5, academicYear);
            stmt.setInt(6, Integer.parseInt(semesterValue));

            // Thực thi câu lệnh
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Khóa học đã được thêm vào cơ sở dữ liệu thành công!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
