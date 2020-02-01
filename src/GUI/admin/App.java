package GUI.admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class App extends Application {
    private static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage=primaryStage;
        gotoLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
    public static void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
    public static void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static boolean showConfirmationAlert(String msg){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(msg);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

    // change scenes
    public static void gotoLogin() throws IOException {
        Parent root=FXMLLoader.load(App.class.getResource("loginAdmin.fxml"));
        stage.setTitle("Login Dashboard");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }
    public static void gotoDashboard() throws IOException {
        Parent root=FXMLLoader.load(App.class.getResource("dashboard.fxml"));
        stage.setTitle("Admin Dashboard");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.setOnCloseRequest(windowEvent -> {});
        stage.setMaximized(true);
        stage.show();
    }
}