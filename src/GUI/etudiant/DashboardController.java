package GUI.etudiant;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Test;

import java.util.ArrayList;

public class DashboardController {

    @FXML
    private Label newTestCount;

    @FXML
    private Label nomEtudiant;

    @FXML
    private Label groupeEtudiant;

    @FXML
    private ScrollPane newTestsPane;

    @FXML
    private ScrollPane oldTestsPane;

    public void initialize() {
        ArrayList<Test> allTests = null;
        try {
            allTests = App.getEmitter().getTests();
        } catch (Exception e) {
            e.printStackTrace();
        }

        nomEtudiant.setText(App.getLoggedEtudiant().getPrenom() + " " + App.getLoggedEtudiant().getNom());
        groupeEtudiant.setText("ID Groupe : " + App.getLoggedEtudiant().getIdGroupe());
        newTestCount.setText(Integer.toString(allTests.size()));

        VBox content = new VBox();
        content.setSpacing(8);
        content.setPadding(new Insets(8));
        for (Test test : allTests) {
            content.getChildren().addAll(createTestRow(test));
        }
        newTestsPane.setContent(content);
    }

    public JFXButton createTestRow(Test test) {
        JFXButton row = new JFXButton();
        row.setButtonType(JFXButton.ButtonType.RAISED);
        row.setStyle("-fx-background-color: #fff");
        row.setPrefHeight(56);
        row.setPrefWidth(320);
        VBox vBox = new VBox();
        Label subtitleLabel = new Label(test.getDetails());
        Label titleLabel = new Label(test.getTitre());
        titleLabel.setStyle("-fx-font-weight: bold");
        subtitleLabel.setStyle("-fx-font-size: 12");
        vBox.getChildren().addAll(titleLabel, subtitleLabel);
        row.setGraphic(vBox);
        row.setOnAction(e -> {
            openTest(test.getId());
        });
        return row;
    }

    public void handleLogout() {
        App.setLoggedEtudiant(null);
        App.gotoLogin();
    }

    public void openTest(int idTest) {
        try {
            App.setActiveTest(App.getEmitter().getTestById(idTest));
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Test.fxml"));
            Parent root;
            root = fxmlLoader.load();
            Scene scene = new Scene(root, 1024, 720);
            scene.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Online Tests");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            App.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }


}
