package GUI.etudiant;

import GUI.Common;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.*;
import util.ServerOfflineException;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private ProgressBar tempsRestantProgressBar;
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
    private int timerSeconds;

    private int currentQuestion;
    private ArrayList<VBox> questionBoxesList = new ArrayList<>();
    private ArrayList<Reponse> reponsesList = new ArrayList<>();

    private DashboardController dashboardController;

    private Boolean isPanne = false;

    private String filePath;

    private String newFilePath;

    public Boolean getPanne() {
        return isPanne;
    }

    public void setPanne(Boolean panne) {
        isPanne = panne;
    }


    public void initialize() {
        nomEtudiant.setText(App.getLoggedEtudiant().getPrenom() + " " + App.getLoggedEtudiant().getNom());
        groupeEtudiant.setText(App.getLoggedEtudiant().getNomGroupe());
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

            for (int j = 1; j <= question.getNombreChoix(); j++) {
                JFXCheckBox checkBox = new JFXCheckBox(Integer.toString(j));
                checkBox.setCheckedColor(Color.web("#046dd5"));
                int choix = j;
                checkBox.setOnAction(e -> {
                    if (checkBox.isSelected()) {
                        addChoixToReponse(reponse, choix);
                    } else {
                        removeChoixFromReponse(reponse, choix);
                    }
                    writeTestToTempFile();
                });

                checkOldValues(question.getId(), reponse, checkBox, j);

                checkboxes.getChildren().add(checkBox);
            }

            questionBox.getChildren().addAll(questionNumber, questionText, checkboxes);
            questionBoxesList.add(questionBox);

            reponsesList.add(reponse);
        }
        currentQuestion = 0;
        questionPane.getChildren().setAll(questionBoxesList.get(currentQuestion));
        prevQuestionButton.setDisable(true);
        if (questionBoxesList.size() == 1) nextQuestionButton.setDisable(true);

        envoyerFicheButton.setOnAction(e -> {
            showSaveAndExitDialog(true);
        });

        Platform.runLater(() -> {
            stage = (Stage) questionPane.getScene().getWindow();
            if (App.getActiveTest().isLocked()) {
                stage.setFullScreenExitHint("");
                stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                stage.setFullScreen(true);
                stage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null && !newValue)
                        stage.setFullScreen(true);
                });
                new Thread(() -> {
                    while (true) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(() -> {
                            stage.toFront();
                        });
                    }
                }).start();
            }
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
        if (!isPanne) {
            timerSeconds = App.getActiveTest().getDuration() * 60;
        }
        int duration = App.getActiveTest().getDuration();
        int interval = 2;
        tempsRestant.setText(LocalTime.MIN.plus(Duration.ofMinutes(timerSeconds / 60)).toString());
        tempsRestantProgressBar.setProgress((double) (duration - timerSeconds / 60) / duration);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (timerSeconds > 0) {
                    Platform.runLater(() -> {
                        tempsRestantProgressBar.setProgress((double) (duration * 60 - timerSeconds) / (duration * 60));
                        if (timerSeconds % 60 == 0)
                            tempsRestant.setText(LocalTime.MIN.plus(Duration.ofMinutes(timerSeconds)).toString());
                    });
                    timerSeconds -= interval;
                } else {
                    Platform.runLater(() -> {
                        showSaveAndExitDialog(false);
                    });
                    timer.cancel();
                }
            }
        }, interval * 1000, interval * 1000);
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

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
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
            File file;
            if (filePath == null) {
                file = new File(newFilePath);
            } else {
                file = new File(filePath);
            }
            file.delete();
            dashboardController.loadTests();
        } catch (Exception e) {
            if (e instanceof ServerOfflineException) {
                dashboardController.getServerStatus().setText("Hors ligne");
                dashboardController.getServerStatus().setStyle("-fx-text-fill: red");
                Common.showErrorAlert("Le serveur est hors ligne, l'application va se fermer, " +
                        "votre test est sauvegardé, veuillez essayer plus tard");
                Platform.exit();
            }
            e.printStackTrace();
        }
    }

    private void checkOldValues(int id_question, Reponse reponse, JFXCheckBox checkBox, int choix) {
        String oldValue = getOldQuestionValue(id_question);
        if (oldValue != null) {
            if (oldValue.contains(Integer.toString(choix))) {
                checkBox.setSelected(true);
                addChoixToReponse(reponse, choix);
            }
        }
    }

    private String getOldQuestionValue(int id_question) {
        try {
            Stream<Path> walk = Files.walk(Paths.get("temp/"));
            List<String> result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".test")).collect(Collectors.toList());
            if (result.size() > 1) {
                Common.showErrorAlert("Une activité suspecte a été detéctée");
                Platform.exit();
            } else if (result.size() == 1) {
                filePath = result.get(0);
                FileInputStream fileInputStream = new FileInputStream(result.get(0));
                ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
                Temp temp = (Temp) inputStream.readObject();
                isPanne = true;
                timerSeconds = temp.getMinute() * 60;
                System.out.println("TS 2 : " + timerSeconds);
                for (Temp.TempReponse tempReponse : temp.getReponses()) {
                    if (tempReponse.getId_question() == id_question) {
                        return tempReponse.getValue();
                    }
                }
            }
        } catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void writeTestToTempFile() {
        try {
            String fullName = App.getLoggedEtudiant().getPrenom() + "-" + App.getLoggedEtudiant().getNom();
            newFilePath = "temp/" + App.getActiveTest().getTitre() + "-" + fullName + ".test";
            FileOutputStream file = new FileOutputStream(newFilePath);
            ObjectOutputStream outputStream = new ObjectOutputStream(file);
            Temp temp = new Temp();
            temp.setId_test(App.getActiveTest().getId());
            temp.setCne(App.getLoggedEtudiant().getCNE());
            temp.setReponses(Temp.toTempReponse(reponsesList));
            temp.setMinute(timerSeconds / 60);
            System.out.println("write  : " + temp);
            outputStream.writeObject(temp);
            outputStream.close();
        } catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }

}
