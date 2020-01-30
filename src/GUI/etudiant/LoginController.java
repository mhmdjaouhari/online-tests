package GUI.etudiant;

import GUI.Common;
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
            Etudiant responseEtudiant = App.getEmitter().login(usernameField.getText(), passwordField.getText());
            App.setLoggedEtudiant(responseEtudiant);
            App.gotoDashboard();
            return true;
        } catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}
