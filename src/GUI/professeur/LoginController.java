package GUI.professeur;

import GUI.GUI;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import models.Etudiant;
import models.Professeur;

public class LoginController {
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;

    public boolean handleLogin() {
        try {
            Professeur professeur = new Professeur();
            professeur.setUsername(usernameField.getText());
            professeur.setPassword(passwordField.getText());
            Professeur responseProfesseur = App.getEmitter().login(professeur);
            App.setLoggedProfesseur(responseProfesseur);
            App.gotoDashboard();
            return true;
        } catch (Exception e) {
            GUI.showErrorAlert(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}
