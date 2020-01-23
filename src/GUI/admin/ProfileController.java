package GUI.admin;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import models.Admin;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML JFXTextField nomField;
    @FXML JFXTextField prenomField;
    @FXML JFXTextField usernameField;
    @FXML JFXPasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public Admin getAdmin(){
        return  null;
    }

    public void updateAdmin(ActionEvent actionEvent) {

    }

    public void Cancel(ActionEvent actionEvent) {

    }

}
