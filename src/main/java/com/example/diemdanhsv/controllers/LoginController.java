package com.example.diemdanhsv.controllers;

import com.example.diemdanhsv.models.User;
import com.example.diemdanhsv.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;

    private final UserRepository userRepository;

    public LoginController() {
        this.userRepository = new UserRepository();
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Vui lòng nhập đầy đủ thông tin!", true);
            return;
        }

        try {
            User user = userRepository.login(username, password);
            
            if (user != null) {
                if (user.isFirstLogin()) {
                    // Nếu là lần đăng nhập đầu tiên, mở form đổi mật khẩu
                    openChangePasswordForm(user);
                } else {
                    // Mở form chính tương ứng với role của user
                    openMainMenuForm(user);
                }
                
                // Đóng form login
                closeLoginForm();
            } else {
                showMessage("Tên đăng nhập hoặc mật khẩu không đúng!", true);
            }
        } catch (Exception e) {
            showMessage("Lỗi: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void openChangePasswordForm(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/diemdanhsv/ChangePassword.fxml"));
            Parent root = loader.load();

            ChangePasswordController controller = loader.getController();
            controller.setCurrentUser(user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Đổi mật khẩu");
            stage.show();
        } catch (Exception e) {
            showError("Lỗi", "Không thể mở form đổi mật khẩu: " + e.getMessage());
        }
    }

    private void openMainMenuForm(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/diemdanhsv/MainMenu.fxml"));
            Parent root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setCurrentUser(user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Chính");
            stage.show();
        } catch (Exception e) {
            showError("Lỗi", "Không thể mở form menu chính: " + e.getMessage());
        }
    }

    private void closeLoginForm() {
        ((Stage) usernameField.getScene().getWindow()).close();
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + (isError ? "red" : "green"));
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

