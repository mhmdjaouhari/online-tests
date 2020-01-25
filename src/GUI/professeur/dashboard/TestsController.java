package GUI.professeur.dashboard;

import GUI.GUI;
import GUI.professeur.App;
import GUI.professeur.TestFormController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import models.Test;

import java.util.ArrayList;

public class TestsController {

    public ScrollPane testsScrollPane;

    public void initialize() {
        ArrayList<Test> allTests = App.getEmitter().getTests(App.getLoggedProfesseur());

        VBox content = new VBox();
        content.setSpacing(8);
        content.setPadding(new Insets(8));
        for (Test test : allTests) {
            content.getChildren().addAll(createTestRow(test));
        }
        testsScrollPane.setContent(content);
    }

    public JFXButton createTestRow(Test test) {
        JFXButton row = new JFXButton();
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setButtonType(JFXButton.ButtonType.RAISED);
        row.setRipplerFill(Paint.valueOf("#046dd5"));
        row.setStyle("-fx-background-color: #fff");
        row.setPrefHeight(64);
        row.setPrefWidth(640);
        Label titleLabel = new Label(test.getTitre());
        titleLabel.setStyle("-fx-font-size: 24");
        Label detailsLabel = new Label(test.getDetails());
        detailsLabel.setTextFill(Paint.valueOf("#666"));
        Label groupesText = new Label("Groupe(s) : ");
        groupesText.setTextFill(Paint.valueOf("#666"));
        Label groupesNoms = new Label(test.getGroupesString());
        groupesNoms.setTextFill(Paint.valueOf("#046dd5"));
        HBox groupesLine = new HBox(groupesText, groupesNoms);
        row.setGraphic(new VBox(
                titleLabel,
                detailsLabel,
                groupesLine
        ));
        row.setOnAction(e -> {
            showTestForm(test);
        });
        return row;
    }

    public void showTestForm(){
        showTestForm(null);
    }

    private void showTestForm(Test test) {
        try {
            Stage testStage = new Stage();
            testStage.initOwner(App.getStage());
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
                controller.setFieldValues(App.getEmitter().getTest(test.getId())); // TODO @achkari: should provide full test with questions!!!!!
            }
            testStage.show();
        } catch (Exception e) {
            GUI.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }
}
