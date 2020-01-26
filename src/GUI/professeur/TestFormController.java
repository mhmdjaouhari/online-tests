package GUI.professeur;

import GUI.Common;
import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import models.Groupe;
import models.Question;
import models.Reponse;
import models.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

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

    @FXML
    private JFXButton submitButton;

    private ArrayList<Groupe> groupesList = new ArrayList<>();
    private ArrayList<Question> questionsList = new ArrayList<>();
    private ArrayList<JFXTextArea> questionTextesList = new ArrayList<>();

    private Test activeTest = new Test();

    private Boolean choicesSaved = true;
    private Boolean isUpdate = false;

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
        isUpdate = true;
        activeTest = test;
        testDurationField.setText(Integer.toString(test.getDuration()));
        titreTestField.setText(test.getTitre());
        lockedToggle.setSelected(test.isLocked());
        penaliteToggle.setSelected(test.isPenalite());
        for (Groupe groupe : test.getGroupes()) {
            addGroupe(groupe);
        }
        for (Question question : test.getQuestions()) {
            addQuestion(question,true);
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
        addQuestion(new Question(),false);
        System.out.println(questionsList);
    }

    public void addQuestion(Question question,Boolean isUpdate) {
        //System.out.println(question);
        JFXTextArea questionText = new JFXTextArea();
        questionText.setFocusColor(Paint.valueOf("#046dd5"));
        questionText.setPromptText("Texte de la question");
        questionText.setPrefColumnCount(40);
        questionText.setLabelFloat(true);
        questionText.setText(question.getTexte());
        JFXButton deleteQuestionButton = new JFXButton("Effacer question");
        deleteQuestionButton.setStyle("-fx-background-color: #ccc");
        deleteQuestionButton.setButtonType(JFXButton.ButtonType.RAISED);
        Label choixLabel = new Label("Choix : Cochez le(s) choix correct(s) ");
        JFXButton addChoiceButton = new JFXButton("+");
        addChoiceButton.setStyle("-fx-background-color: #ccc");
        addChoiceButton.setButtonType(JFXButton.ButtonType.RAISED);
        addChoiceButton.setAlignment(Pos.BOTTOM_CENTER);
        VBox questionControls = new VBox(deleteQuestionButton,choixLabel,addChoiceButton);

        questionControls.setSpacing(12);
        questionControls.setMinWidth(160);
        questionControls.setPrefHeight(200);
        if(isUpdate){
            Integer choiceId=1;
            ArrayList<String> choicesText = new ArrayList<>();
            Collections.addAll(choicesText,question.getAnswersTexte().split(",",0));
            for(String choiceText:choicesText){
                addChoix(question,choiceText,choiceId,questionControls, true,addChoiceButton);
                choiceId++;
            }
        }else {
            int numberOfChoices = question.getAnswersTexte().split(",",0).length;
            addChoix(question,"",numberOfChoices+1,questionControls, false,addChoiceButton);
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
        addChoiceButton.setOnAction((e)->{
            int numberOfChoices = question.getAnswersTexte().split(",",0).length;
            addChoix(question,"",numberOfChoices+1,questionControls,false,addChoiceButton);
            addChoiceButton.setDisable(true);

        });
    }

    private void addChoix(
            Question question,String choiceText,Integer choiceId,
            VBox questionControls,Boolean isUpdate,JFXButton addChoiceButton
    ){
       // System.out.println("id_qst: "+question.getId()+" | choiceId: "+choiceId+" | answerText: "+choiceText+" | Value: "+question.getValue());
        JFXCheckBox checkBox = new JFXCheckBox();
        TextField choiceField = new TextField(choiceText);

        choiceField.setStyle("-fx-background-color: #ccc");
        JFXButton editButton = null;
        JFXButton deleteButton = new JFXButton("Delete");
        if(isUpdate){
            choiceField.setDisable(true);
            checkBox.setDisable(true);
            deleteButton.setDisable(false);
            editButton = new JFXButton("Edit");
        }
        else{
            deleteButton.setDisable(true);
            editButton = new JFXButton("Save");
            editButton.setDisable(true);
            choicesSaved = false;
        }

        JFXButton finalEditButton = editButton;
        choiceField.setOnKeyTyped(e->{
            if(choiceField.getText().trim().equals("")){
                finalEditButton.setDisable(true);
            }else {
                finalEditButton.setDisable(false);
            }
        });
        editButton.setOnAction((e)->{
            if(choiceField.isDisable()){
                choiceField.setDisable(false);
                checkBox.setDisable(false);
                deleteButton.setDisable(true);
                finalEditButton.setText("Save");
                addChoiceButton.setDisable(true);
                choicesSaved = false;
                removeAnswerFromText(question,choiceField.getText());
            }else{
                choiceField.setDisable(true);
                finalEditButton.setText("Edit");
                checkBox.setDisable(true);
                deleteButton.setDisable(false);
                int numberOfAnswers = question.getAnswersTexte().split(",",0).length;
                if(isColumnSaved(questionControls)){
                    choicesSaved = true;
                }
                if(numberOfAnswers<4){
                    addChoiceButton.setDisable(false);
                }
                if(!addAnswerToAnswersTexte(question,choiceField.getText())){
                    choiceField.setDisable(false);
                    checkBox.setDisable(false);
                    deleteButton.setDisable(true);
                }
            }
        });
        deleteButton.setOnAction((e)->{
            HBox source = ((HBox)((JFXButton)e.getSource()).getParent());
            VBox parentSource = (VBox)(source.getParent());
            int index = parentSource.getChildren().indexOf(source);
            ArrayList<String> answers = new ArrayList<>();
            Collections.addAll(answers,question.getAnswersTexte().split(",",0));
            answers.remove(choiceField.getText());
            question.setAnswersTexte(String.join(",",answers));

            questionsBox.getChildren().remove(new HBox(checkBox,choiceField,finalEditButton,deleteButton));
            removeChoixFromQuestion(question,choiceId);

            addChoiceButton.setDisable(false);

            int numberOfAnswers = question.getAnswersTexte().split(",",0).length;
            System.out.println(numberOfAnswers);
            if(numberOfAnswers == 1){
                //deleteButton.setDisable(true);
                ((JFXButton)(((HBox)(questionControls.getChildren().get(3))).getChildren().get(3))).setDisable(true);
            }
            //System.out.println("238# values : "+question.getValue());
        });
        HBox choiceRow = new HBox(checkBox,choiceField,editButton,deleteButton);
        choiceRow.setAlignment(Pos.CENTER);
        checkBox.setCheckedColor(Color.web("#046dd5"));
        checkBox.setOnAction(e -> {
            if (checkBox.isSelected()){
                addChoixToQuestion(question, choiceId);
            }
            else{
                removeChoixFromQuestion(question, choiceId);
            }
        });
        ArrayList<String> values = new ArrayList<>();
        Collections.addAll(values,question.getValue().split(",",0));
        if(values.contains(Integer.toString(choiceId))){
            checkBox.setSelected(true);
        }
        questionControls.getChildren().add(choiceRow);
        //addChoixToQuestion(question,choiceId);
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

    private void removeChoixFromQuestion(Question question, int choiceId) {
        ArrayList<String> values = new ArrayList<>();
        Collections.addAll(values,question.getValue().split(",",0));
        if(values.remove(Integer.toString(choiceId))){
            String newValue = String.join(",",values);
            System.out.println(newValue);
            question.setValue(newValue);
            if(!question.getValue().equals("")){
                shiftValues(question,choiceId);
            }
        }
    }

    private Boolean addAnswerToAnswersTexte(Question question,String answer){
        ArrayList<String> answers = new ArrayList<>();
        Collections.addAll(answers,question.getAnswersTexte().split(",",0));
        //System.out.println("## answers : "+answers);
        if(answers.contains(answer)){
            Common.showErrorAlert("Ce choix existe déja, veuillez le modifier");

            return false;
        }
        if(question.getAnswersTexte().equals("")){
            question.setAnswersTexte(answer);
        }else{
            question.setAnswersTexte(question.getAnswersTexte()+","+answer);
        }
        return true;
    }

    private void removeAnswerFromText(Question question,String answer){
        ArrayList<String> answers = new ArrayList<>();
        Collections.addAll(answers,question.getAnswersTexte().split(",",0));
        //System.out.println("## answers : "+answers);
        if(!answers.contains(answer)){
            Common.showErrorAlert("choix n'existe pas");
            return;
        }
        answers.remove(answer);
        question.setAnswersTexte(String.join(",",answers));

    }

    private Boolean isColumnSaved(VBox questionControls){
        for(Object children:questionControls.getChildren()){
            if(children instanceof HBox){
                for(Object subChild:((HBox) children).getChildren()){
                    if(subChild instanceof TextField){
                        if(!((TextField) subChild).isDisable()){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }





    private void shiftValues(Question question,int start){
        String[] values = question.getValue().split(",",0);
        System.out.println("#352# values : "+String.join(",",values));
        for(int i=0;i<values.length;i++){
            if(Integer.parseInt(values[i])>start){
                values[i] = Integer.toString(Integer.parseInt(values[i])-1);
            }
        }
        question.setValue(String.join(",",values));
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

    public String whatWrong(){
        String result = "";
        if(titreTestField.getText().trim().equals("")){
            result = result + "Le titre du teste ne peut pas être vide\n\n";
        }
        if(testDurationField.getText().trim().equals("")){
            result = result + "La duration ne peut pas être vide !\n\n";
        }
        if(groupesList.size() == 0){
            result = result + "Il faut choisir au moin un groupe\n\n";
        }
        if(questionsList.size() == 0){
            result = result + "Le teste doit posseder au moin une question\n\n";
        }
        if(!choicesSaved){
            result = result + "Quelques choix ne sont pas enregistrés\n\n";
        }
        return result;
    }

    @FXML
    private void submitTest()  {
        try {
            String wrongs = whatWrong();
            if(!wrongs.equals("")){
                throw new Exception(wrongs);
            }
            activeTest.setDuration(Integer.parseInt(testDurationField.getText()));
            activeTest.setLocked(lockedToggle.isSelected());
            activeTest.setPenalite(penaliteToggle.isSelected());
            activeTest.setMatriculeProf(App.getLoggedProfesseur().getMatricule());
            activeTest.setTitre(titreTestField.getText());
            activeTest.setGroupes(groupesList);
            System.out.println(questionsList.size());
            for (int i = 0; i < questionsList.size(); i++) {
                //System.out.println(questionsList.get(i));
                questionsList.get(i).setTexte(questionTextesList.get(i).getText());
            }
            System.out.println(questionsList);
//            for(Question question:questionsList){
//                question.setTexte(que);
//            }
            activeTest.setQuestions(questionsList);
            if(isUpdate){
                App.getEmitter().updateTest(activeTest.getId(),activeTest);
            }else{
                App.getEmitter().createTest(activeTest);
            }
            Stage stage = (Stage)submitButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }

}
