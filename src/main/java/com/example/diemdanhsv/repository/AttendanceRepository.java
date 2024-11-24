package com.example.diemdanhsv.repository;

import com.example.diemdanhsv.database.DatabaseConnection;
import com.example.diemdanhsv.models.Attendance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AttendanceRepository {

    // Phương thức lấy danh sách Attendance từ cơ sở dữ liệu
    public ObservableList<Attendance> getAttendanceList() {
        ObservableList<Attendance> attendanceList = FXCollections.observableArrayList();
        String query = "SELECT a.id, a.student_id, a.course_id, a.date, a.status, s.name AS student_name, s.gender " +
                       "FROM attendance a " +
                       "JOIN students s ON a.student_id = s.id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Attendance attendance = new Attendance(
                        resultSet.getInt("id"),
                        resultSet.getInt("student_id"),
                        resultSet.getInt("course_id"),
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getString("status"),
                        resultSet.getString("student_name"),
                        resultSet.getString("gender")
                );

                attendanceList.add(attendance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attendanceList;
    }

}
