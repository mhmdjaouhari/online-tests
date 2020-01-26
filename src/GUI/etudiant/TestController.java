package GUI.etudiant;

import GUI.Common;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.*;


import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
    @FXML
    private JFXButton envoyerFicheButton;

    private Stage stage;

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

            ArrayList<String> answers = new ArrayList<>();
            Collections.addAll(answers,question.getAnswersTexte().split(",",0));

            int k=1;
            for(String answer:answers){
                JFXCheckBox checkBox = new JFXCheckBox(answer);
                checkBox.setCheckedColor(Color.web("#046dd5"));
                int choix = k;
                checkBox.setOnAction(e -> {
                    if (checkBox.isSelected())
                        addChoixToReponse(reponse, choix);
                    else
                        removeChoixFromReponse(reponse, choix);
                });
                checkboxes.getChildren().add(checkBox);
                k++;
            }

//            for (int j = 1; j <= 4; j++) {
//                JFXCheckBox checkBox = new JFXCheckBox(Integer.toString(j));
//                checkBox.setCheckedColor(Color.web("#046dd5"));
//                int choix = j;
//                checkBox.setOnAction(e -> {
//                    if (checkBox.isSelected())
//                        addChoixToReponse(reponse, choix);
//                    else
//                        removeChoixFromReponse(reponse, choix);
//                });
//                checkboxes.getChildren().add(checkBox);
//            }
            questionBox.getChildren().addAll(questionNumber, questionText, checkboxes);
            questionBoxesList.add(questionBox);

            reponsesList.add(reponse);
        }
        currentQuestion = 0;
//        System.out.println("size of question boxe: "+questionBoxesList.size());
        questionPane.getChildren().setAll(questionBoxesList.get(currentQuestion));
        prevQuestionButton.setDisable(true);

        envoyerFicheButton.setOnAction(e -> {
            showSaveAndExitDialog(true);
        });

        Platform.runLater(() -> {
            stage = (Stage) questionPane.getScene().getWindow();
            stage.setOnCloseRequest(e -> {
                if (App.getLoggedEtudiant() != null) {
                    e.consume();
                    showSaveAndExitDialog(true);
                }
            });
        });

        setTimer();
    }

    private void setTimer() {
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
                    showSaveAndExitDialog(false);
                }
            }
        }, 60 * 1000, 60 * 1000);
    }

    public boolean showSaveAndExitDialog(boolean isExitVoluntary) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(stage);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.setResizable(true);
        dialog.getDialogPane().setPrefSize(320, 160);
        if (isExitVoluntary) {
            dialog.setTitle("Quitter le test");
            dialog.setContentText("En quittant le test vous envoyez votre fiche de réponse actuelle. Êtes-vous sûr de vouloir quitter ?");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
        } else {
            dialog.setTitle("Fin du test");
            dialog.setContentText("La durée du test est terminée. Votre fiche de réponses sera envoyée au serveur.");
        }
        AtomicBoolean returnValue = new AtomicBoolean(false);
        dialog.setOnCloseRequest(e -> {
            ButtonType result = dialog.getResult();
            if (result == ButtonType.OK) {
                submitFiche();
                timer.cancel();
                stage.close();
                App.setActiveTest(null);
                returnValue.set(true);
            }
        });
        dialog.showAndWait();
        return returnValue.get();
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
        String newValue;
        if (!oldValue.equals("")) {
            HashSet<String> choixArray = new HashSet<>(Arrays.asList(oldValue.split(",")));
            choixArray.add(Integer.toString(choix));
            newValue = String.join(",", choixArray);
        } else {
            newValue = Integer.toString(choix);
        }
        reponse.setValue(newValue);
    }

    private void removeChoixFromReponse(Reponse reponse, int choix) {
        String oldValue = reponse.getValue();
        if (!oldValue.equals("")) {
            HashSet<String> choixArray = new HashSet<>(Arrays.asList(oldValue.split(",")));
            choixArray.remove(Integer.toString(choix));
            String newValue = String.join(",", choixArray);
            reponse.setValue(newValue);
        }
    }

    private void submitFiche() {
        Fiche fiche = new Fiche();
        fiche.setCNE(App.getLoggedEtudiant().getCNE());
        fiche.setReponses(reponsesList);
        Test test = new Test();
        test.setId(App.getActiveTest().getId());
        fiche.setTest(test);
        try {
            App.getEmitter().submitFiche(fiche);
        } catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }

}
