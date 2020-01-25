package GUI.professeur;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import models.Groupe;
import models.Question;
import models.Reponse;
import models.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class TestFormController {
    @FXML
    private JFXTextField testDurationField;
    @FXML
    private JFXTextField titreTestField;
    @FXML
    private JFXToggleButton lockedToggle;
    @FXML
    private JFXToggleButton penaliteToggle;
    @FXML
    private JFXComboBox<Groupe> groupesComboBox;
    @FXML
    private HBox groupesBox;
    @FXML
    private VBox questionsBox;

    private ArrayList<Groupe> groupesList = new ArrayList<>();
    private ArrayList<Question> questionsList = new ArrayList<>();
    private ArrayList<JFXTextArea> questionTextesList = new ArrayList<>();

    private Test activeTest;

    public void initialize() {
        // force the field to be numeric only & limit length to 3 digits
        testDurationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                testDurationField.setText(newValue.replaceAll("[^\\d]", ""));
            if (testDurationField.getText().length() > 3)
                testDurationField.setText(testDurationField.getText().substring(0, 3));
        });
        groupesComboBox.getItems().setAll(App.getEmitter().getGroupes());
    }

    public void setFieldValues(Test test) {
        activeTest = test;
        testDurationField.setText(Integer.toString(test.getDuration()));
        titreTestField.setText(test.getTitre());
        lockedToggle.setSelected(test.isLocked());
        penaliteToggle.setSelected(test.isPenalite());
        for (Groupe groupe : test.getGroupes()) {
            addGroupe(groupe);
        }
        for (Question question : test.getQuestions()) {
            addQuestion(question);
            questionsList.add(question);
        }
    }

    public void addGroupe() {
        addGroupe(groupesComboBox.getValue());
    }

    public void addGroupe(Groupe groupe) {
        Label groupeNom = new Label(groupe.getNom());
        groupeNom.setTextFill(Paint.valueOf("#fff"));
        JFXButton deleteGroupeButton = new JFXButton("×");
        deleteGroupeButton.setStyle("-fx-background-color: #eee; -fx-font-size: 20");
        deleteGroupeButton.setPadding(new Insets(0, 6, 0, 6));
        HBox groupeChip = new HBox(groupeNom, deleteGroupeButton);
        groupeChip.setPadding(new Insets(6));
        groupeChip.setSpacing(6);
        groupeChip.setStyle("-fx-background-color: #999; -fx-border-radius: 6px; -fx-background-radius: 6px");
        groupeChip.setAlignment(Pos.CENTER);
        if (!groupesList.contains(groupe)) {
            groupesList.add(groupe);
            groupesBox.getChildren().add(groupeChip);
        }
        deleteGroupeButton.setOnAction(e -> {
            deleteGroupe(groupe, groupeChip);
        });
    }

    private void deleteGroupe(Groupe groupe, HBox groupeChip) {
        groupesBox.getChildren().remove(groupeChip);
        groupesList.remove(groupe);
    }

    public void addQuestion() {
        addQuestion(new Question());
    }

    public void addQuestion(Question question) {
        JFXTextArea questionText = new JFXTextArea();
        questionText.setFocusColor(Paint.valueOf("#046dd5"));
        questionText.setPromptText("Texte de la question");
        questionText.setPrefColumnCount(40);
        questionText.setLabelFloat(true);
        JFXButton deleteQuestionButton = new JFXButton("Effacer question");
        deleteQuestionButton.setStyle("-fx-background-color: #ccc");
        deleteQuestionButton.setButtonType(JFXButton.ButtonType.RAISED);
        Label choixLabel = new Label("Choix correct(s) :");
        VBox questionControls = new VBox(deleteQuestionButton, choixLabel);
        questionControls.setSpacing(12);
        questionControls.setMinWidth(160);
        questionControls.setPrefHeight(200);
        for (int j = 1; j <= 4; j++) {
            JFXCheckBox checkBox = new JFXCheckBox(Integer.toString(j));
            // TODO: add default values for checkboxes when editing test
            checkBox.setCheckedColor(Color.web("#046dd5"));
            int choix = j;
            checkBox.setOnAction(e -> {
                if (checkBox.isSelected())
                    addChoixToQuestion(question, choix);
                else
                    removeChoixFromQuestion(question, choix);
            });
            questionControls.getChildren().add(checkBox);
        }
        HBox questionRow = new HBox(questionText, questionControls);
        questionRow.setPadding(new Insets(6));
        questionRow.setSpacing(12);
        questionRow.setPrefHeight(128);

        questionTextesList.add(questionText);
        questionsList.add(question);
        questionsBox.getChildren().add(questionRow);
        deleteQuestionButton.setOnAction(e -> {
            deleteQuestion(question, questionRow, questionText);
        });
    }

    private void deleteQuestion(Question question, HBox questionRow, JFXTextArea questionText) {
        questionTextesList.remove(questionText);
        questionsBox.getChildren().remove(questionRow);
        questionsList.remove(question);
    }

    private void addChoixToQuestion(Question question, int choix) {
        String oldValue = question.getValue();
        String newValue;
        if (!oldValue.equals("")) {
            HashSet<String> choixArray = new HashSet<>(Arrays.asList(oldValue.split(",")));
            choixArray.add(Integer.toString(choix));
            newValue = String.join(",", choixArray);
        } else {
            newValue = Integer.toString(choix);
        }
        question.setValue(newValue);
    }

    private void removeChoixFromQuestion(Question question, int choix) {
        String oldValue = question.getValue();
        String newValue;
        if (!oldValue.equals("")) {
            HashSet<String> choixArray = new HashSet<>(Arrays.asList(oldValue.split(",")));
            choixArray.add(Integer.toString(choix));
            newValue = String.join(",", choixArray);
        } else {
            newValue = Integer.toString(choix);
        }
        question.setValue(newValue);
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

    @FXML
    private void submitTest() {
        activeTest.setDuration(Integer.parseInt(testDurationField.getText()));
        activeTest.setLocked(lockedToggle.isSelected());
        activeTest.setPenalite(penaliteToggle.isSelected());
        activeTest.setMatriculeProf(App.getLoggedProfesseur().getMatricule());
        activeTest.setTitre(titreTestField.getText());
        activeTest.setGroupes(groupesList);
        for (int i = 0; i < questionsList.size(); i++) {
            questionsList.get(i).setTexte(questionTextesList.get(i).getText());
        }
        activeTest.setQuestions(questionsList);
        // TODO @achkari : try catch (submitTest should throw Exception)
        App.getEmitter().submitTest(activeTest);
    }

}
