package GUI.admin;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Etudiant;
import models.Groupe;
import server.DAOs.AdminDAO;
import server.DAOs.EtudiantDAO;
import server.dispatchers.EtudiantDispatcher;
import server.dispatchers.ProfesseurDispatcher;
import util.Action;
import util.Request;
import util.Response;
import util.Role;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class EtudiantsController implements Initializable {
    @FXML JFXComboBox<String> groups;
    @FXML JFXComboBox<String> filterGroupe;
    // fields contents
    @FXML JFXTextField searchField;
    @FXML JFXTextField CNEField;
    @FXML JFXTextField nomField;
    @FXML JFXTextField prenomField;
    @FXML JFXTextField usernameField;
    @FXML JFXPasswordField passwordField;
    @FXML JFXTextField GroupField;

    // config the table
    @FXML TableView<Etudiant> tab;
    @FXML TableColumn<Etudiant, String> cne;
    @FXML TableColumn<Etudiant, String> nom;
    @FXML TableColumn<Etudiant, String> prenom;
    @FXML TableColumn<Etudiant, String> username;

    boolean isEditClicked=false;
    Etudiant oldEtud;
    ObservableList<Etudiant> DataEtud;
    ArrayList<Groupe> grps;
    // Wrap the ObservableList in a FilteredList (initially display all data).
    FilteredList<Etudiant> filteredData;

       @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // table initialization
        cne.setCellValueFactory(new PropertyValueFactory<>("CNE"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        initTable(-1);
        // combobox initialization
        initCombobox();
    }

    // btnEdit
    public void EditEtudiant(ActionEvent actionEvent) {
        if (!tab.getSelectionModel().isEmpty()){
            oldEtud=tab.getSelectionModel().getSelectedItem();
            isEditClicked=true;
            charge(oldEtud);
        }
    }
    // btnSave
    public void SaveEtudiant(ActionEvent actionEvent) {
        if(isFieldEmpty()) App.showErrorAlert("all fields are requires !");
        else
        {
            if(isEditClicked){
            Etudiant newEtud=load();
            System.out.println("Selected student : "+newEtud);
            ArrayList<Etudiant> etudiants=new ArrayList<Etudiant>();
            etudiants.add(oldEtud);
            etudiants.add(newEtud);
            Response res=EtudiantDispatcher.handle(new Request(Action.UPDATE_ETUDIANT,etudiants, Role.ETUDIANT));
            dialog(res);
            cancel();
            }
            else
            {
            Response res=EtudiantDispatcher.handle(new Request(Action.CREATE_ETUDIANT,load(), Role.ETUDIANT));
            dialog(res);
            cancel();
            }
        }
    }
    // btnDelete
    public void DeleteEtudiant(ActionEvent actionEvent) {
        if (!tab.getSelectionModel().isEmpty()){
            Etudiant etd=tab.getSelectionModel().getSelectedItem();
            if(App.showConfirmationAlert("You want delete student :\n "+etd.getNom()+" "+etd.getPrenom())){
                Response res=EtudiantDispatcher.handle(new Request(Action.DELETE_ETUDIANT,etd, Role.ETUDIANT));
                dialog(res);
                cancel();
            }
        }
    }
    // btn cancel
    public void cancel() {
        CNEField.setText("");
        CNEField.setDisable(false);
        nomField.setText("");
        prenomField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        groups.setValue("");
        isEditClicked=false;
    }

    // btnSaveGroup
    public void SaveGroup(ActionEvent actionEvent) {
        if(!GroupField.getText().isEmpty()){
            String nomGroup=GroupField.getText();
            Response res= AdminDAO.AddGroup(nomGroup);
            dialog(res);
            cancel();
            initCombobox();
        }
    }
    // btnCancelGroupField
    public void cancelGroupField(ActionEvent actionEvent) {
           GroupField.setText("");
    }

    // Combobox FilterGroup
    public void getFiltredGroup(ActionEvent actionEvent) {
        initTable(getIdGroup(filterGroupe.getValue()));
    }

    ////////////////***Utils***//////////////////////
    // Dialog
    public  void dialog(Response res){
        if(res.getStatus()!=0)
        {
            System.out.println("Alert smthg wrong: "+res.getMessage());
            App.showErrorAlert(res.getMessage());
        }
        else {
            App.showSuccessAlert(res.getMessage());
            System.out.println(res.getMessage());
            initTable(getIdGroup(filterGroupe.getValue()));
        }
    }
    // charge object to textFiels
    private void charge(Etudiant oldEtud) {
        CNEField.setText(oldEtud.getCNE());
        CNEField.setDisable(true);
        nomField.setText(oldEtud.getNom());
        prenomField.setText(oldEtud.getPrenom());
        usernameField.setText(oldEtud.getUsername());
        passwordField.setText(oldEtud.getPassword());
        groups.setValue(getNomGroup(oldEtud.getIdGroupe()));
        // groups.setVisibleRowCount(1);
    }
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
    public  int getIdGroup(String groupeName){
      for (Groupe grp: grps)
          if(groupeName==grp.getNom())
              return grp.getId();
      return -1;
    }
    // to get the name of group
    public  String getNomGroup(int id_grp){
        for (Groupe grp: grps)
            if(id_grp==grp.getId())
                return grp.getNom();
        return "";
    }
    // to get all groups ( inorder to use it in the combobox )
    public  ArrayList<Groupe> getAllGroupes(){
        ArrayList<Groupe> groups;
        Response res=ProfesseurDispatcher.handle(new Request(Action.GET_GROUPES, Role.PROFESSEUR));
        if(res.getStatus()!=0)
        {
            System.out.println("Alert smthg wrong: "+res.getMessage());
            return null;
        }
        else {
            groups=(ArrayList<Groupe>)res.getData();
            return groups;
        }
    }
    // to get all students
    public  ObservableList<Etudiant> getAllEtudiants(int id) {
        ObservableList<Etudiant> AllEtud;
        Response res= EtudiantDispatcher.handle(new Request(Action.GET_ALL_ETUDIANTS,id, Role.ETUDIANT));
        if(res.getStatus()!=0)
        {
            System.out.println("Alert smthg wrong: "+res.getMessage());
            return null;
        }
        else {
            AllEtud=(ObservableList<Etudiant>)res.getData();
            //System.out.println(AllEtud.get(0));
            return AllEtud;
        }
    }
    // test validation of fields !
    public boolean isFieldEmpty(){
        if(CNEField!=null && nomField!=null && prenomField!=null && usernameField!=null && passwordField !=null && groups.getValue()!=null) {
            if (!CNEField.getText().isEmpty() && !nomField.getText().isEmpty() && !prenomField.getText().isEmpty() && !usernameField.getText().isEmpty() && !passwordField.getText().isEmpty() && (!groups.getValue().isEmpty())) {
                return false;
            }
            else
            return true;
        }
        else
        return true;
    }
    // init the table
    public void initTable(int id){
        DataEtud=getAllEtudiants(id);
        filteredData= new FilteredList<>(DataEtud, b -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(etudiant -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (etudiant.getNom().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true;
                } else if (etudiant.getPrenom().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                else if (etudiant.getCNE().indexOf(lowerCaseFilter)!=-1){
                    return true;
                }
                else if (etudiant.getUsername().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                else
                    return false;
            });
        });
        SortedList<Etudiant> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tab.comparatorProperty());
        tab.setItems(sortedData);
    }
    // init the combobox
    private void initCombobox() {
        grps=getAllGroupes();
        filterGroupe.getItems().add("ALL");
       for(Groupe grp : grps){
            filterGroupe.getItems().add(grp.getNom());
            groups.getItems().add(grp.getNom());
          }
        filterGroupe.setValue("ALL");
    }

}
