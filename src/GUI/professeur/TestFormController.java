package GUI.professeur;

import GUI.Common;
import GUI.professeur.dashboard.TestsController;
import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import models.Groupe;
import models.Question;
import models.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

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

    private TestsController testsController;

    private ArrayList<Groupe> groupesList = new ArrayList<>();
    private ArrayList<Question> questionsList = new ArrayList<>();
    private ArrayList<JFXTextArea> questionTextesList = new ArrayList<>();

    private Test activeTest = new Test();

    public void initialize() {
        // force the field to be numeric only & limit length to 3 digits
        testDurationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                testDurationField.setText(newValue.replaceAll("[^\\d]", ""));
            if (testDurationField.getText().length() > 3)
                testDurationField.setText(testDurationField.getText().substring(0, 3));
        });
        try {
            groupesComboBox.getItems().setAll(App.getEmitter().getGroupes());
        } catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
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
        }
    }

    public void setTestsController(TestsController testsController) {
        this.testsController = testsController;
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
        questionText.setText(question.getTexte());
        JFXButton deleteQuestionButton = new JFXButton("Effacer question");
        deleteQuestionButton.setStyle("-fx-background-color: #ccc");
        deleteQuestionButton.setButtonType(JFXButton.ButtonType.RAISED);
        deleteQuestionButton.setFocusTraversable(false);
        Label nombreChoixLabel = new Label("Nombre de choix :");

        Label choixLabel = new Label("Choix correct(s) :");

        AtomicReference<ArrayList<String>> correctChoixList = new AtomicReference<>(new ArrayList<>(Arrays.asList(question.getValue().split(","))));
        if (question.getNombreChoix() == 0) question.setNombreChoix(2);
        int nombreChoix = question.getNombreChoix();
        JFXSlider nombreChoixSlider = new JFXSlider(2, 5, nombreChoix);
        nombreChoixSlider.setMajorTickUnit(1);
        nombreChoixSlider.setMinorTickCount(0);
        nombreChoixSlider.setShowTickLabels(true);
        nombreChoixSlider.setShowTickMarks(true);
        nombreChoixSlider.setSnapToTicks(true);

        VBox questionControls = new VBox(deleteQuestionButton, nombreChoixLabel, nombreChoixSlider, choixLabel);
        questionControls.setSpacing(12);
        questionControls.setMinWidth(160);
        questionControls.setPrefHeight(200);
        questionControls.getChildren().add(allChoix(nombreChoix, correctChoixList.get(), question));

        nombreChoixSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            correctChoixList.set(new ArrayList<>(Arrays.asList(question.getValue().split(","))));
            question.setNombreChoix(newValue.intValue());
            questionControls.getChildren().set(4, allChoix(newValue.intValue(), correctChoixList.get(), question));
        });

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

    private VBox allChoix(int nombreChoix, ArrayList<String> correctChoixList, Question question) {
        VBox allChoix = new VBox();
        allChoix.setSpacing(8);
        for (int j = 1; j <= nombreChoix; j++) {
            JFXCheckBox checkBox = new JFXCheckBox(Integer.toString(j));
            checkBox.setSelected(correctChoixList.contains(Integer.toString(j)));
            checkBox.setCheckedColor(Color.web("#046dd5"));
            int choix = j;
            checkBox.setOnAction(e -> {
                if (checkBox.isSelected())
                    addChoixToQuestion(question, choix);
                else
                    removeChoixFromQuestion(question, choix);
            });
            allChoix.getChildren().add(checkBox);
        }
        return allChoix;
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
        if (!oldValue.equals("")) {
            HashSet<String> choixArray = new HashSet<>(Arrays.asList(oldValue.split(",")));
            choixArray.remove(Integer.toString(choix));
            String newValue = String.join(",", choixArray);
            question.setValue(newValue);
        }
    }

    public void validateInputs() throws Exception {
        String result = "";
        if (titreTestField.getText().trim().equals("")) {
            result = result + "• Le titre du test ne peut pas être vide\n";
        }
        if (testDurationField.getText().trim().equals("")) {
            result = result + "• La duration ne peut pas être vide\n";
        }
        if (groupesList.size() == 0) {
            result = result + "• Il faut choisir au moins un groupe\n";
        }
        if (questionsList.size() == 0) {
            result = result + "• Le test doit posséder au moins une question\n";
        }
        if (!result.equals(""))
            throw new Exception(result);
    }

    @FXML
    private void submitTest() {
        try {
            validateInputs();

            activeTest.setDuration(Integer.parseInt(testDurationField.getText()));
            activeTest.setLocked(lockedToggle.isSelected());
            activeTest.setPenalite(penaliteToggle.isSelected());
            activeTest.setMatriculeProf(App.getLoggedProfesseur().getMatricule());
            activeTest.setTitre(titreTestField.getText());
            activeTest.setGroupes(groupesList);
            System.out.println(questionsList.size());
            for (int i = 0; i < questionsList.size(); i++) {
                questionsList.get(i).setTexte(questionTextesList.get(i).getText());
                String oldValue = questionsList.get(i).getValue();
                ArrayList<String> choixArray = new ArrayList<>(Arrays.asList(oldValue.split(",")));
                ArrayList<String> newChoixArray = new ArrayList<>();
                for (String s : choixArray) {
                    if (Integer.parseInt(s) <= questionsList.get(i).getNombreChoix())
                        newChoixArray.add(s);
                }
                String newValue = String.join(",", newChoixArray);
                questionsList.get(i).setValue(newValue);
                System.out.println(questionsList.get(i));
            }
            activeTest.setQuestions(questionsList);

            if (activeTest.getId() == 0) {
                App.getEmitter().createTest(activeTest);
            } else {
                App.getEmitter().updateTest(activeTest.getId(), activeTest);
            }
            Stage stage = (Stage)titreTestField.getScene().getWindow();
            stage.close();
            testsController.loadTests();
        }
        catch (NumberFormatException nfe){
            Common.showErrorAlert("Il doit y avoir au moins une réponse juste");
        }
        catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }

    }

}