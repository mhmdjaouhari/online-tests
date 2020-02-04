package GUI.professeur.dashboard;

import GUI.Common;
import GUI.professeur.App;
import GUI.professeur.TestFormController;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Fiche;
import models.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TestsController {

    @FXML
    private ScrollPane testsScrollPane;

    public void initialize() {
        loadTests();
    }

    public void loadTests() {
        testsScrollPane.setFitToWidth(true);
        try {
            ArrayList<Test> allTests = App.getEmitter().getProfesseursTests(App.getLoggedProfesseur().getMatricule());
            VBox content = new VBox();
            content.setSpacing(8);
            content.setPadding(new Insets(8));
            for (int i = allTests.size() - 1; i >= 0; i--) {
                content.getChildren().add(createTestRow(allTests.get(i)));
            }
            testsScrollPane.setContent(content);
        } catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }

    private HBox createTestRow(Test test) {
        HBox row = new HBox();
        VBox.setVgrow(row, Priority.ALWAYS);
        row.setSpacing(8);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setStyle("-fx-background-color: #fff; -fx-border-radius: 5px; -fx-background-radius: 5px");
        row.setPrefHeight(64);
        row.setMinWidth(640);
        row.setPrefWidth(2560);
        Label titleLabel = new Label(test.getTitre());
        titleLabel.setStyle("-fx-font-size: 24");
        Label detailsLabel = new Label(test.getDetails());
        detailsLabel.setTextFill(Paint.valueOf("#666"));
        Label groupesText = new Label("Groupe(s) : ");
        groupesText.setTextFill(Paint.valueOf("#666"));
        Label groupesNoms = new Label(test.getGroupesString());
        groupesNoms.setTextFill(Paint.valueOf("#046dd5"));
        HBox groupesLine = new HBox(groupesText, groupesNoms);
        VBox description = new VBox(titleLabel, detailsLabel, groupesLine);
        HBox.setHgrow(description, Priority.ALWAYS);
        row.getChildren().add(description);

        JFXButton editButton = new JFXButton("Modifier");
        editButton.setButtonType(JFXButton.ButtonType.RAISED);
        editButton.setStyle("-fx-background-color: #ddd; -fx-font-size: 14");
        editButton.setOnAction(e -> {
            showTestForm(test);
        });
        JFXButton consultFichesButton = new JFXButton("Consulter fiches");
        consultFichesButton.setButtonType(JFXButton.ButtonType.RAISED);
        consultFichesButton.setStyle("-fx-background-color: #ddd; -fx-font-size: 14");
        consultFichesButton.setOnAction(e -> {
            showTestFiches(test);
        });
        JFXButton publishResultsButton = new JFXButton("Publier résultats");
        publishResultsButton.setButtonType(JFXButton.ButtonType.RAISED);
        publishResultsButton.setStyle("-fx-background-color: #ddd; -fx-font-size: 14");
        publishResultsButton.setOnAction(e -> {
            try {
                App.getEmitter().publishTestResults(test.getId());
                loadTests();
            } catch (Exception ex) {
                Common.showErrorAlert(ex.getMessage());
                ex.printStackTrace();
            }
        });
        if(test.isResultsPublished()){
            publishResultsButton.setDisable(true);
        }
        VBox buttons = new VBox(editButton, consultFichesButton, publishResultsButton);
        buttons.setSpacing(4);
        buttons.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(buttons, Priority.NEVER);
        row.getChildren().add(buttons);
        return row;
    }

    public void showTestForm() {
        showTestForm(null);
    }

    private void showTestForm(Test test) {
        try {
            Stage testStage = new Stage();
            testStage.initOwner(App.getStage());
            testStage.initModality(Modality.WINDOW_MODAL);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/GUI/professeur/TestForm.fxml"));
            Parent root;
            root = fxmlLoader.load();
            Scene scene = new Scene(root, 960, 640);
            testStage.setScene(scene);
            TestFormController controller = fxmlLoader.getController();
            if (test == null)
                testStage.setTitle("Créer un nouveau test");
            else {
                testStage.setTitle("Modification de test");
                controller.setFieldValues(App.getEmitter().getFullTestById(test.getId()));
            }
            controller.setTestsController(this);
            testStage.show();
        } catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }

    private void showTestFiches(Test test) {
        try {
            Stage fichesStage = new Stage();
            fichesStage.initOwner(App.getStage());
            fichesStage.setMaxWidth(672);
//            fichesStage.initModality(Modality.WINDOW_MODAL);
            VBox vBox = new VBox();
            vBox.setFillWidth(true);
            vBox.setSpacing(8);
            vBox.setPadding(new Insets(16));
            ScrollPane scrollPane = new ScrollPane(vBox);
            Scene scene = new Scene(scrollPane, 960, 640);
            scene.getStylesheets().add(getClass().getResource("/GUI/gui.css").toExternalForm());
            fichesStage.setScene(scene);
            fichesStage.setTitle("Fiches du test \""+ test.getTitre() +"\"");
            Label label = new Label("Fiches du test \""+ test.getTitre() +"\"");
            label.setStyle("-fx-font-size: 26");
            vBox.getChildren().add(label);
            label.setPadding(new Insets(8));
            ArrayList<Fiche> fichesList = App.getEmitter().getTestFiches(test.getId());
            for (int i = fichesList.size() - 1; i >= 0; i--) {
                Fiche fiche = fichesList.get(i);
                vBox.getChildren().add(createOldTestRow(fiche));
            }
            fichesStage.show();
        } catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }

    public JFXButton createOldTestRow(Fiche fiche) throws Exception {
        Test test = fiche.getTest();
        JFXButton row = new JFXButton();
        row.setButtonType(JFXButton.ButtonType.RAISED);
        row.setRipplerFill(Paint.valueOf("#046dd5"));
        row.setStyle("-fx-background-color: #ccc");
        row.setPrefHeight(56);
        row.setMinWidth(304);
        row.setPrefWidth(640);
//        row.setDisable(true);
        VBox vBox = new VBox();
        Label subtitleLabel = new Label(test.getDetails());
        Label titleLabel = new Label(test.getTitre());
        Label bylineLabel = new Label("Par : " + test.getNomProf());
        titleLabel.setStyle("-fx-font-weight: bold");
        subtitleLabel.setStyle("-fx-font-size: 12");
        bylineLabel.setStyle("-fx-font-size: 12");
        vBox.getChildren().addAll(titleLabel, subtitleLabel, bylineLabel);
        String note = (new DecimalFormat("#.#")).format(fiche.getNote());
        Label noteLabel = new Label(note);
        noteLabel.setTextFill(Paint.valueOf("#f00"));
        Label baremeLabel = new Label("20");
        baremeLabel.setTextFill(Paint.valueOf("#f00"));
        baremeLabel.setStyle("-fx-border-color: #f00; -fx-border-width: 1px 0 0 0");
        VBox noteBox = new VBox(noteLabel, baremeLabel);
        noteBox.setAlignment(Pos.BASELINE_CENTER);
        noteBox.setStyle("-fx-border-color: #f00; -fx-border-radius: 100%");
        noteBox.setMinSize(44, 48);
        noteBox.setMaxSize(44, 48);
//        noteBox.setPrefSize(32,32);
        row.setGraphic(new HBox(vBox, noteBox));
        HBox.setHgrow(vBox, Priority.ALWAYS);
        HBox.setHgrow(noteBox, Priority.NEVER);
        return row;
    }
}
