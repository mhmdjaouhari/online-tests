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
import models.Test;

import java.util.ArrayList;

public class TestsController {

    @FXML
    private ScrollPane testsScrollPane;

    public void initialize() {
        loadTests();
    }

    public void loadTests() {
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
//            showTestFiches(test);
        });
        JFXButton publishResultsButton = new JFXButton("Publier résultats");
        publishResultsButton.setButtonType(JFXButton.ButtonType.RAISED);
        publishResultsButton.setStyle("-fx-background-color: #ddd; -fx-font-size: 14");
        publishResultsButton.setOnAction(e -> {
            try {
                App.getEmitter().publishTestResults(test.getId());
            } catch (Exception ex) {
                Common.showErrorAlert(ex.getMessage());
                ex.printStackTrace();
            }
        });
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
}
