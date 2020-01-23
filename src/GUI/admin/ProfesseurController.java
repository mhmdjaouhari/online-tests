package GUI.admin;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import models.Professeur;
import server.dispatchers.ProfesseurDispatcher;
import util.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProfesseurController implements Initializable {

    @FXML JFXTextField nomField;
    @FXML JFXTextField prenomField;
    @FXML JFXTextField usernameField;
    @FXML JFXPasswordField passwordField;
    @FXML JFXTextField matriculeField;
    // config the table
    @FXML
    TableView<Professeur> tab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initialize the profs table
    }

    public void createProf(ActionEvent actionEvent) {
        Professeur newProf=load();
        Response res= ProfesseurDispatcher.handle(new Request(Action.CREATE_PROF, newProf,Role.PROFESSEUR));
        if(res.getStatus()==1)
        {
            System.out.println("Alert smthg wrong: "+res.getMessage());
            App.showErrorAlert(res.getMessage());
        }
        else {
            App.showSuccessAlert(res.getMessage());
            System.out.println(res.getMessage());
        }
    }

    public void updateProf(ActionEvent actionEvent) {
        ArrayList<Professeur> arrayEtud= new ArrayList<Professeur>();
        // arraylist of 2 prof old and new
        Professeur oldProf=null; // we need the Matricule
        Professeur newProf=load(); // from inputs
        //arrayEtud.add(oldEtud,newEtud);
        //....
    }

    public void deleteProf(ActionEvent actionEvent) {
        Professeur prof=new Professeur();
        // from table row
        prof.setMatricule("123");
        // here we can add confirmation dialog
        Response res=ProfesseurDispatcher.handle(new Request(Action.DELETE_PROF,prof,Role.PROFESSEUR));
        if(res.getStatus()==1)
        {
            System.out.println("Alert smthg wrong: "+res.getMessage());
            App.showErrorAlert(res.getMessage());
        }
        else {
            App.showSuccessAlert(res.getMessage());
            System.out.println(res.getMessage());
            // refresh the tableView ...
        }
    }

    // help funct:

    // to load prof object from admin inputs
    public Professeur load() {
        Professeur prof=new Professeur();
        prof.setMatricule(matriculeField.getText());
        prof.setNom(nomField.getText());
        prof.setPrenom(prenomField.getText());
        prof.setUsername(usernameField.getText());
        prof.setPassword(passwordField.getText());
        return prof;
    }

    // to load all the profs
    public static ArrayList<Professeur> getAllProfs(){
        ArrayList<Professeur> AllProfs;
        Response res= ProfesseurDispatcher.handle(new Request(Action.GET_ALL_PROFS, Role.PROFESSEUR));
        if(res.getStatus()==1)
        {
            System.out.println("Alert smthg wrong: "+res.getMessage());
            App.showErrorAlert(res.getMessage());
            return null;
        }
        else {
            AllProfs=(ArrayList<Professeur>)res.getData();
            System.out.println(AllProfs.get(0));
            return AllProfs;
        }
    }

}
