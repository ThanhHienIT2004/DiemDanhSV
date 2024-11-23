package com.example.diemdanhsv.controllers;

import com.example.diemdanhsv.models.Attendance;
import com.example.diemdanhsv.models.Student;
import com.example.diemdanhsv.models.User;
import com.example.diemdanhsv.repository.AttendanceRepository;
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
    private Button addButton;
    @FXML
    private Button removeButton;

    @FXML
    public TextField idField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField userIdField;
    @FXML
    public TextField genderField;

    @FXML
    private ComboBox<String> courseComboBox;
    @FXML
    private TableView<Student> attendanceTable;
    @FXML
    private TableColumn<Student, Integer> idColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> genderColumn;
    @FXML
    private TableColumn<Student, String> statusColumn;

    private ObservableList<Student> studentList;

    private AttendanceRepository attendanceRepository;

    private User currentUser;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        attendanceRepository = new AttendanceRepository();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        studentList = FXCollections.observableArrayList();
        attendanceTable.setItems(studentList);

        loadCourses();
        loadAttendanceData();
    }

    private void loadAttendanceData() {
        ObservableList<Attendance> attendanceList = attendanceRepository.getAttendanceList();
        for (Attendance attendance : attendanceList) {
            Student student = new Student(
                attendance.getStudentId(),
                attendance.getStudentName(),
                attendance.getStudentId(),
                attendance.getGender(),
                attendance.getStatus()
            );
            studentList.add(student);
        }
    }

    public void add(ActionEvent e) {
        try {
            Student newStudent = new Student();
            newStudent.setId(Integer.parseInt(idField.getText()));
            newStudent.setName(nameField.getText());
            newStudent.setUserId(Integer.parseInt(userIdField.getText()));
            newStudent.setGender(genderField.getText());

            studentList.add(newStudent);

            idField.clear();
            nameField.clear();
            userIdField.clear();
            genderField.clear();
        } catch (NumberFormatException ex) {
            showAlert("Input Error", "Please enter valid data!");
        }
    }

    public void remove(ActionEvent e) {
        Student selectedStudent = attendanceTable.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            showAlert("No Selection", "Please select a student to remove.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText("Delete Student");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            studentList.remove(selectedStudent);
            showAlert("Success", "Student deleted successfully.");
        }
    }

    private void loadCourses() {
        CourseRepository queryHandle = new CourseRepository();
        ObservableList<String> courses = queryHandle.getCourses();
        courseComboBox.setItems(courses);

        if (courses.isEmpty()) {
            showAlert("No Data", "No courses found in the database.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null) {
            switch (user.getRole()) {
                case "ADMIN":
                    enableAllControls();
                    break;
                case "TEACHER":
                    enableTeacherControls();
                    break;
                case "STUDENT":
                    enableStudentControls();
                    break;
            }
        }
    }

    private void enableAllControls() {
        addButton.setDisable(false);
        removeButton.setDisable(false);
        courseComboBox.setDisable(false);
    }

    private void enableTeacherControls() {
        addButton.setDisable(false);
        removeButton.setDisable(false);
        courseComboBox.setDisable(false);
    }

    private void enableStudentControls() {
        addButton.setDisable(true);
        removeButton.setDisable(true);
        courseComboBox.setDisable(true);
    }
}