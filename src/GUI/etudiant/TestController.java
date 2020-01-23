package GUI.etudiant;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Question;
import models.Reponse;

import java.util.ArrayList;

public class TestController {

    @FXML
    private Label nomEtudiant;
    @FXML
    private Label groupeEtudiant;
    @FXML
    private Label titreTest;
    @FXML
    private Label detailsTest;

    private ArrayList<Question> questionsList;
    private ArrayList<Reponse> reponsesList;


    public void initialize() {
        nomEtudiant.setText(App.getLoggedEtudiant().getPrenom() + " " + App.getLoggedEtudiant().getNom());
        groupeEtudiant.setText("ID Groupe : " + App.getLoggedEtudiant().getIdGroupe());
        titreTest.setText(App.getActiveTest().getTitre());
        detailsTest.setText(App.getActiveTest().getDetails());
        questionsList = App.getActiveTest().getQuestions();
        reponsesList = new ArrayList<>();
    }
}
