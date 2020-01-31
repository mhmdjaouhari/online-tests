package GUI.admin;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import models.Admin;
import models.Professeur;
import server.DAOs.AdminDAO;
import server.DAOs.ProfesseurDAO;
import util.Response;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML JFXTextField nomField;
    @FXML JFXTextField prenomField;
    @FXML JFXTextField usernameField;
    @FXML JFXPasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nomField.setText(DashBoardController.loggeduser.getNom());
        prenomField.setText(DashBoardController.loggeduser.getPrenom());
        usernameField.setText(DashBoardController.loggeduser.getLogin());
        passwordField.setText(DashBoardController.loggeduser.getPassword());
    }

    public Admin getAdmin(){
        return  null;
    }

    public void updateAdmin(ActionEvent actionEvent) {
        if(isFieldEmpty()) App.showErrorAlert("All the fields are required ");
        else
        {
            Response res=AdminDAO.update(DashBoardController.loggeduser,load());
            if(res.getStatus()==1)
            {
                System.out.println("Alert smthg wrong: "+res.getMessage());
                App.showErrorAlert(res.getMessage());
            }
            else {
                App.showSuccessAlert(res.getMessage());
                System.out.println(res.getMessage());
                DashBoardController.loggeduser=(Admin) res.getData();
            }

            System.out.println(res.getData());
        }
    }

    public void Cancel(ActionEvent actionEvent) {
    }


    // Utils :

    public Admin load(){
        Admin ad= new Admin();
        ad.setNom(nomField.getText());
        ad.setLogin(usernameField.getText());
        ad.setPassword(passwordField.getText());
        ad.setPrenom(prenomField.getText());
        return ad;
    }
    public boolean isFieldEmpty() {
        if(nomField!=null && prenomField!=null && usernameField!=null && passwordField !=null)
        {
            if (!nomField.getText().isEmpty() && !prenomField.getText().isEmpty() && !usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
                return false;
            }
            else
                return true;
        }
        else
            return true;
    }
}
