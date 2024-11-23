package com.example.diemdanhsv.repository;

import com.example.diemdanhsv.database.DatabaseConnection;
import com.example.diemdanhsv.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserRepository {
    private Connection connection;

    public UserRepository() {
        this.connection = DatabaseConnection.getConnection();
        if (this.connection == null) {
            throw new RuntimeException("Không thể kết nối database!");
        }
    }

    public User login(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND hashed_password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("hashed_password"),
                    rs.getString("role").toUpperCase(),
                    rs.getBoolean("first_login"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi đăng nhập: " + e.getMessage());
        }
        return null;
    }

    public void closeConnection() {
        if (connection != null) {
            DatabaseConnection.closeConnection(connection);
        }
    }

    public boolean updatePassword(int userId, String newPassword) {
        String query = "UPDATE users SET hashed_password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật mật khẩu: " + e.getMessage());
        }
    }

    public boolean checkPassword(int userId, String currentPassword) {
        String query = "SELECT COUNT(*) FROM users WHERE id = ? AND hashed_password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, currentPassword);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi kiểm tra mật khẩu: " + e.getMessage());
        }
        return false;
    }
}