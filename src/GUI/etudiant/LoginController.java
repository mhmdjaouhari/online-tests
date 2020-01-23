package GUI.etudiant;

import GUI.GUI;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import models.Etudiant;

public class LoginController {
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;

    public boolean handleLogin() {
        try {
            Etudiant etudiant = new Etudiant();
            etudiant.setUsername(usernameField.getText());
            etudiant.setPassword(passwordField.getText());
            Etudiant responseEtudiant = App.getEmitter().login(etudiant);
            App.setLoggedEtudiant(responseEtudiant);
            App.gotoDashboard();
            return true;
        } catch (Exception e) {
            GUI.showErrorAlert(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}
