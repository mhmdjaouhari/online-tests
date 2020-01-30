package GUI.professeur;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Etudiant;

import java.util.ArrayList;

public class GroupeTableController {

    @FXML
    private TableView<Etudiant> tableEtudiants;


    public void loadEtudiants(ArrayList<Etudiant> etudiants) {
        TableColumn<Etudiant, Integer> id = new TableColumn<>("CNE");
        id.setCellValueFactory(new PropertyValueFactory<>("CNE"));
        tableEtudiants.getColumns().add(id);
        TableColumn<Etudiant, String> nom = new TableColumn<>("Nom");
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tableEtudiants.getColumns().add(nom);
        TableColumn<Etudiant, String> prenom = new TableColumn<>("Prénom");
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        tableEtudiants.getColumns().add(prenom);
        tableEtudiants.getItems().addAll(etudiants);
    }
}
