package com.example.diemdanhsv.controllers;

import com.example.diemdanhsv.models.Student;
import com.example.diemdanhsv.repository.CourseRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AttendanceController implements Initializable {
    @FXML
    public TextField idField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField userIdField;

    @FXML
    private ComboBox<String> courseComboBox; // Thay đổi từ subjectComboBox thành courseComboBox
    @FXML
    private TableView<Student> attendanceTable;
    @FXML
    private TableColumn<Student, Integer> idColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> statusColumn;

    private ObservableList<Student> studentList; // Danh sách sinh viên

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Liên kết cột với thuộc tính trong lớp Student
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Gắn danh sách sinh viên vào bảng
        attendanceTable.setItems(studentList);
        // Tải danh sách khóa học từ cơ sở dữ liệu
        loadCourses();
    }

    // Thêm sinh viên mới
    public void add(ActionEvent e) {
        try {
            // Tạo sinh viên mới
            Student newStudent = new Student();
            newStudent.setId(Integer.parseInt(idField.getText()));
            newStudent.setName(nameField.getText());
            newStudent.setUserId(Integer.parseInt(userIdField.getText()));

            // Thêm vào danh sách
            studentList.add(newStudent);

            // Xóa dữ liệu trong các trường nhập
            idField.clear();
            nameField.clear();
            userIdField.clear();
        } catch (NumberFormatException ex) {
            showAlert("Input Error", "Please enter valid data!");
        }
    }

    // Xóa sinh viên được chọn
    public void remove(ActionEvent e) {
        // Lấy sinh viên được chọn từ bảng
        Student selectedStudent = attendanceTable.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            showAlert("No Selection", "Please select a student to remove.");
            return;
        }

        // Hiển thị hộp thoại xác nhận
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText("Delete Student");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            studentList.remove(selectedStudent); // Xóa sinh viên khỏi danh sách
            showAlert("Success", "Student deleted successfully.");
        }
    }
    // Phương thức tải danh sách khóa học vào ComboBox
    private void loadCourses() {
        CourseRepository queryHandle = new CourseRepository();  // Sử dụng CourseQueryHandle thay vì SubjectQueryHandle
        ObservableList<String> courses = queryHandle.getCourses();
        courseComboBox.setItems(courses);

        if (courses.isEmpty()) {
            showAlert("No Data", "No courses found in the database.");
        }
    }
    // Phương thức hiển thị thông báo
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}