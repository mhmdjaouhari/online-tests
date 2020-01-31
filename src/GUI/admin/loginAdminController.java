package GUI.admin;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.Admin;
import models.Etudiant;
import server.DAOs.AdminDAO;
import util.Response;

import java.io.IOException;

public class loginAdminController {
    @FXML private JFXTextField usernameField;
    @FXML private JFXPasswordField passwordField;

    public void login(ActionEvent actionEvent) throws IOException {
        Admin adm=new Admin();
        adm.setPassword(passwordField.getText());
        adm.setLogin(usernameField.getText());

        Response res= AdminDAO.login(adm) ;
        if(res.getStatus()==0){
            DashBoardController.setLogedUser((Admin)res.getData());
            App.gotoDashboard();
        }
        else
            App.showErrorAlert(res.getMessage());
    }
}
