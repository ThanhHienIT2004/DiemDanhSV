package com.example.diemdanhsv.controllers;

import com.example.diemdanhsv.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class MainMenuController {
    private User currentUser;
    
    @FXML
    private Label welcomeLabel;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        // Có thể thêm logic để hiển thị/ẩn các chức năng dựa trên role của user
    }

    @FXML
    private void openAttendance(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/diemdanhsv/Attendance.fxml"));
            Parent root = loader.load();
            
            // Truyền currentUser cho AttendanceController
            AttendanceController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Stage stage = new Stage();
            stage.setTitle("Điểm Danh");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể mở form điểm danh: " + e.getMessage());
        }
    }

    @FXML
    private void openChangePassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/diemdanhsv/ChangePassword.fxml"));
            Parent root = loader.load();
            
            ChangePasswordController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Stage stage = new Stage();
            stage.setTitle("Đổi Mật Khẩu");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể mở form đổi mật khẩu: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Mở lại form login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/diemdanhsv/LoginView.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.show();

            // Đóng form hiện tại
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể đăng xuất: " + e.getMessage());
        }
    }

    private void showError(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
