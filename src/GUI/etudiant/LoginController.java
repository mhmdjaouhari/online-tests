package GUI.etudiant;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import models.Etudiant;

public class LoginController {
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;

    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    public void initialize(){
        setApp(App.getInstance());
    }

    public boolean handleLogin() {
        try {
            Etudiant etudiant = new Etudiant();
            etudiant.setUsername(usernameField.getText());
            etudiant.setPassword(passwordField.getText());
            Etudiant responseEtudiant = App.getEmitter().login(etudiant);
            app.setLoggedEtudiant(responseEtudiant);
            app.gotoDashboard();
            return true;
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
