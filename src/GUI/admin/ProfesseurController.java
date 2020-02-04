package GUI.admin;

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
import models.Professeur;
import server.DAOs.ProfesseurDAO;
import server.dispatchers.ProfesseurDispatcher;
import util.Action;
import util.Request;
import util.Response;
import util.Role;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProfesseurController implements Initializable {

    @FXML JFXTextField nomField;
    @FXML JFXTextField prenomField;
    @FXML JFXTextField usernameField;
    @FXML JFXPasswordField passwordField;
    @FXML JFXTextField matriculeField;
    @FXML JFXTextField searchField;

    // config the table
    @FXML TableView<Professeur> tab;
    @FXML TableColumn<Professeur, String> matricule;
    @FXML TableColumn<Professeur, String> nom;
    @FXML TableColumn<Professeur, String> prenom;
    @FXML TableColumn<Professeur, String> username;

    // utils
    boolean isEditClicked=false;
    Professeur oldProf;
    ObservableList<Professeur> DataProf;
    // Wrap the ObservableList in a FilteredList (initially display all data).
    FilteredList<Professeur> filteredData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initialize the profs table
        matricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        initTable();
    }

    // btnEdit
    public void EditProf(ActionEvent actionEvent) {
        if (!tab.getSelectionModel().isEmpty()){
            oldProf=tab.getSelectionModel().getSelectedItem();
            isEditClicked=true;
            charge(oldProf);
        }
    }
    // btnSave
    public void SaveProf(ActionEvent actionEvent) {
        if(isFieldEmpty()) App.showErrorAlert("plead");
        else
        {
            if(isEditClicked){
                Professeur newProf=load();
                System.out.println("get it "+newProf);
                ArrayList<Professeur> Profs=new ArrayList<Professeur>();
                Profs.add(oldProf);
                Profs.add(newProf);
                Response res= ProfesseurDispatcher.handle(new Request(Action.UPDATE_PROF,Profs,Role.PROFESSEUR));
                dialog(res);
                cancel();
            }
            else
            {
                Response res= ProfesseurDispatcher.handle(new Request(Action.CREATE_PROF,load(),Role.PROFESSEUR));
                dialog(res);
                cancel();
            }
        }
    }
    // btnDelete
    public void DeleteProf(ActionEvent actionEvent) {
        if (!tab.getSelectionModel().isEmpty()){
            Professeur prof=tab.getSelectionModel().getSelectedItem();
            if(App.showConfirmationAlert("You want delete student :\n "+prof.getNom()+" "+prof.getPrenom())){
                Response res=ProfesseurDispatcher.handle(new Request(Action.DELETE_PROF,prof,Role.PROFESSEUR));
                dialog(res);
                cancel();
            }
        }
    }
    // btn cancel
    public void cancel() {
        matriculeField.setDisable(false);
        matriculeField.setText("");
        nomField.setText("");
        prenomField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        isEditClicked=false;
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
            initTable();
        }
    }
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
    // charge object to textFiels
    private void charge(Professeur oldProf) {
        matriculeField.setText(oldProf.getMatricule());
        matriculeField.setDisable(true);
        nomField.setText(oldProf.getNom());
        prenomField.setText(oldProf.getPrenom());
        usernameField.setText(oldProf.getUsername());
        passwordField.setText(oldProf.getPassword());
    }
    // to get all Profs
    public ObservableList<Professeur> getAllProfs() {
        ObservableList<Professeur> AllEtud;
        Response res= ProfesseurDispatcher.handle(new Request(Action.GET_ALL_PROFS, Role.PROFESSEUR));
        if(res.getStatus()==1)
        {
            System.out.println("Alert smthg wrong: "+res.getMessage());
            return null;
        }
        else {
            AllEtud=(ObservableList<Professeur>)res.getData();
            //System.out.println(AllEtud.get(0));
            return AllEtud;
        }
    }
    //  Validation test
    public boolean isFieldEmpty(){
        if(matriculeField!=null && nomField!=null && prenomField!=null && usernameField!=null && passwordField !=null) {
            if (!matriculeField.getText().isEmpty() && !nomField.getText().isEmpty() && !prenomField.getText().isEmpty() && !usernameField.getText().isEmpty() && !passwordField.getText().isEmpty() ) {
                return false;
            }
            else
                return true;
        }
        else
            return true;
    }
    // table initialization
    public void initTable(){
        /////////////////
        DataProf=getAllProfs();
        filteredData= new FilteredList<>(DataProf, b -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(professeur -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (professeur.getNom().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches first name
                } else if (professeur.getPrenom().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name
                }
                else if (professeur.getMatricule().indexOf(lowerCaseFilter)!=-1){
                    return true; // Filter matches Matricule
                }
                else if (professeur.getUsername().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches username
                }
                else
                    return false; // Does not match.
            });
        });
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Professeur> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tab.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        tab.setItems(sortedData);
    }

}
