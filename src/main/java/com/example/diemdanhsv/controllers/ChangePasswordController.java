package com.example.diemdanhsv.controllers;

import com.example.diemdanhsv.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import com.example.diemdanhsv.repository.UserRepository;

public class ChangePasswordController {
    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label messageLabel;

    private User currentUser;
    private UserRepository userRepository;

    public ChangePasswordController() {
        userRepository = new UserRepository();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void handleChangePassword() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (validateInputs(currentPassword, newPassword, confirmPassword)) {
            try {
                if (userRepository.updatePassword(currentUser.getId(), newPassword)) {
                    currentUser.setHashedPassword(newPassword);
                    currentUser.setFirstLogin(false);
                    showSuccess("Đổi mật khẩu thành công!");
                    closeWindow();
                } else {
                    showError("Có lỗi xảy ra khi cập nhật mật khẩu!");
                }
            } catch (Exception e) {
                showError("Có lỗi xảy ra: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private boolean validateInputs(String currentPassword, String newPassword, String confirmPassword) {
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("Vui lòng điền đầy đủ thông tin!");
            return false;
        }

        if (!userRepository.checkPassword(currentUser.getId(), currentPassword)) {
            showError("Mật khẩu hiện tại không đúng!");
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError("Mật khẩu mới không khớp!");
            return false;
        }

        if (newPassword.length() < 6) {
            showError("Mật khẩu mới phải có ít nhất 6 ký tự!");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        messageLabel.setText(message);
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        ((Stage) currentPasswordField.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancel() {
        ((Stage) currentPasswordField.getScene().getWindow()).close();
    }
}