package GUI.professeur;

import GUI.Common;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import models.Professeur;

public class LoginController {
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;

    public boolean handleLogin() {
        try {

            Professeur responseProfesseur = App.getEmitter().login(usernameField.getText(), passwordField.getText());
            App.setLoggedProfesseur(responseProfesseur);
            App.gotoDashboard();
            return true;
        } catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}
