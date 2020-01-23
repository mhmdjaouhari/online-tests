package GUI.admin;

import com.jfoenix.controls.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import models.Etudiant;
import server.dispatchers.EtudiantDispatcher;
import util.*;
import java.net.URL;
import java.util.*;

public class EtudiantsController implements Initializable {
    @FXML
    JFXComboBox<String> groups;
    // fields contents
    @FXML JFXTextField CNEField;
    @FXML JFXTextField nomField;
    @FXML JFXTextField prenomField;
    @FXML JFXTextField usernameField;
    @FXML JFXPasswordField passwordField;

    // config the table
    @FXML
    TableView<Etudiant> tab;
    @FXML
    TableColumn<Etudiant, String> cne;
    @FXML TableColumn<Etudiant, String> nom;
    @FXML TableColumn<Etudiant, String> prenom;
    @FXML TableColumn<Etudiant, String> groupe;
    @FXML TableColumn<Etudiant, String> username;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // table initialization here is some problems !!
//        cne.setCellValueFactory(new PropertyValueFactory<>("CNE"));
//        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
//        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
//        groupe.setCellValueFactory(new PropertyValueFactory<>("nomGroupe"));
//        username.setCellValueFactory(new PropertyValueFactory<>("username"));
//        tab.setItems(getAllEtudiants());

        // initialize combobox
        List<String> grps=getAllGroupes();
        if ( grps!=null) groups.getItems().addAll(grps);
    }

    public void createEtudiant(ActionEvent actionEvent) {
        Response res=EtudiantDispatcher.handle(new Request(Action.CREATE_ETUDIANT,load(),Role.ETUDIANT));
        if(res.getStatus()==1)
        {
            System.out.println("Alert smthg wrong: "+res.getMessage());
            App.showErrorAlert(res.getMessage());
        }
        else {
            App.showSuccessAlert(res.getMessage());
            System.out.println(res.getMessage());
        }
        System.out.println(load());
    }

    public void updateEtudiant(ActionEvent actionEvent) {
        ArrayList<Etudiant> arrayEtud= new ArrayList<Etudiant>();
        Etudiant oldEtud=null;
        Etudiant newEtud=load();

        //arrayEtud.add(oldEtud,newEtud);
    }

    public void deleteEtudiant(ActionEvent actionEvent) {
        Etudiant etd=new Etudiant();
        // from table row
        etd.setCNE("123");
        Response res=EtudiantDispatcher.handle(new Request(Action.DELETE_ETUDIANT,etd,Role.ETUDIANT));
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

    // some hlp functions : //

    // load object from textFields
    public Etudiant load() {
        Etudiant etd=new Etudiant();
        etd.setCNE(CNEField.getText());
        etd.setNom(nomField.getText());
        etd.setPrenom(prenomField.getText());
        etd.setUsername(usernameField.getText());
        etd.setPassword(passwordField.getText());
        etd.setIdGroupe(getIdGroup(groups.getValue()));
        return etd;
    }
    // to get the id of the group name
    public static int getIdGroup(String groupeName){
        Response res=EtudiantDispatcher.handle(new Request(Action.GET_ID_GROUP,groupeName,Role.ETUDIANT));
        if(res.getStatus()==1)
        {
            System.out.println("Alert smthg wrong: "+res.getMessage());
            return 0;
        }
        else {
            return (int)res.getData();
        }
    }
    // to get all groups ( inorder to use it in the combobox )
    public static ArrayList<String> getAllGroupes(){
        ArrayList<String> groups;
        Request req=new Request(Action.GET_ALL_GROUPS, Role.ETUDIANT);
        Response res=EtudiantDispatcher.handle(req);
        if(res.getStatus()==1)
        {
            System.out.println("Alert smthg wrong: "+res.getMessage());
            return null;
        }
        else {
            groups=(ArrayList<String> )res.getData();
            return groups;
        }
    }
    // to get all students
    public static ObservableList<Etudiant> getAllEtudiants() {
        ObservableList<Etudiant> AllEtud;
        Request req=new Request(Action.GET_ALL_ETUDIANTS, Role.ETUDIANT);
        Response res=EtudiantDispatcher.handle(req);
        if(res.getStatus()==1)
        {
            System.out.println("Alert smthg wrong: "+res.getMessage());
            return null;
        }
        else {
            AllEtud=(ObservableList<Etudiant>)res.getData();
            System.out.println(AllEtud.get(0));
            return AllEtud;
        }
    }
}
