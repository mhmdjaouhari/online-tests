package GUI.etudiant;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import models.Question;
import models.Reponse;


import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class TestController {

    @FXML
    private Label nomEtudiant;
    @FXML
    private Label groupeEtudiant;
    @FXML
    private Label titreTest;
    @FXML
    private Label detailsTest;
    @FXML
    private Label tempsRestant;
    @FXML
    private VBox questionPane;
    @FXML
    private JFXButton prevQuestionButton;
    @FXML
    private JFXButton nextQuestionButton;


    private Timer timer;
    private int timerMinutes;

    private int currentQuestion;
    private ArrayList<VBox> questionBoxesList = new ArrayList<>();
    private ArrayList<Reponse> reponsesList = new ArrayList<>();


    public void initialize() {
        nomEtudiant.setText(App.getLoggedEtudiant().getPrenom() + " " + App.getLoggedEtudiant().getNom());
        groupeEtudiant.setText("ID Groupe : " + App.getLoggedEtudiant().getIdGroupe());
        titreTest.setText(App.getActiveTest().getTitre());
        detailsTest.setText(App.getActiveTest().getDetails());
        reponsesList = new ArrayList<>();
        int i = 0;
        for (Question question : App.getActiveTest().getQuestions()) {
            VBox questionBox = new VBox();
            VBox.setVgrow(questionBox, Priority.ALWAYS);
            questionBox.setAlignment(Pos.CENTER);
            questionBox.setSpacing(32);
            Label questionNumber = new Label("Question #" + (++i));
            questionNumber.setFont(new Font("Ubuntu Bold", 22));
            Label questionText = new Label(question.getTexte());
            HBox checkboxes = new HBox();
            checkboxes.setSpacing(32);
            checkboxes.setAlignment(Pos.CENTER);

            Reponse reponse = new Reponse();
            reponse.setIdQuestion(question.getId());

            for (int j = 1; j <= 4; j++) {
                JFXCheckBox checkBox = new JFXCheckBox(Integer.toString(j));
                checkBox.setCheckedColor(Color.web("#046dd5"));
                int choix = j;
                checkBox.setOnAction(e -> {
                    if(checkBox.isSelected())
                        addChoixToReponse(reponse, choix);
                    else
                        removeChoixFromReponse(reponse, choix);
                });
                checkboxes.getChildren().add(checkBox);
            }
            questionBox.getChildren().addAll(questionNumber, questionText, checkboxes);
            questionBoxesList.add(questionBox);

            reponsesList.add(reponse);
        }
        currentQuestion = 0;
        questionPane.getChildren().setAll(questionBoxesList.get(currentQuestion));
        prevQuestionButton.setDisable(true);

        setTimer();
    }

    public void setTimer() {
        timer = new Timer();
        timerMinutes = App.getActiveTest().getDuration();
        tempsRestant.setText(LocalTime.MIN.plus(Duration.ofMinutes(timerMinutes)).toString());
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (timerMinutes > 0) {
                    Platform.runLater(() -> tempsRestant.setText(LocalTime.MIN.plus(Duration.ofMinutes(timerMinutes)).toString()));
                    timerMinutes--;
                } else {
                    timer.cancel();
                    // INTERRUPT THE TEST, SUBMIT FICHE...
                }

            }
        }, 60 * 1000, 60 * 1000);
    }

    public void showPrevQuestion(ActionEvent actionEvent) {
        if (currentQuestion > 0)
            currentQuestion--;
        if (currentQuestion == 0)
            prevQuestionButton.setDisable(true);
        nextQuestionButton.setDisable(false);
        questionPane.getChildren().setAll(questionBoxesList.get(currentQuestion));
    }

    public void showNextQuestion(ActionEvent actionEvent) {
        int n = questionBoxesList.size();
        if (currentQuestion < n - 1)
            currentQuestion++;
        if (currentQuestion == n - 1)
            nextQuestionButton.setDisable(true);
        prevQuestionButton.setDisable(false);
        questionPane.getChildren().setAll(questionBoxesList.get(currentQuestion));
    }

    public void addChoixToReponse(Reponse reponse, int choix) {
        String oldValue = reponse.getValue();
        HashSet<String> choixArray = new HashSet<>(Arrays.asList(oldValue.split("(?!^)")));
        choixArray.add(Integer.toString(choix));
        StringBuilder newValue = new StringBuilder();
        for (String choixValue : choixArray) {
            newValue.append(choixValue);
        }
        reponse.setValue(newValue.toString());
    }

    private void removeChoixFromReponse(Reponse reponse, int choix) {
        String oldValue = reponse.getValue();;
        String newValue = oldValue.replace(Integer.toString(choix), "");
        reponse.setValue(newValue);
    }

}
