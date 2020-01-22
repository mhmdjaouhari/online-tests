package GUI.etudiant;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
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

    private TestController testController;

    public void initialize() {
        ArrayList<Test> allTests = App.getEmitter().getTests();

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

        Platform.runLater(() -> {
            Stage stage = (Stage) newTestsPane.getScene().getWindow();
            stage.setOnCloseRequest(e -> {
                if (App.getLoggedEtudiant() != null &&
                        App.getActiveTest() != null &&
                        testController != null &&
                        !testController.showSaveAndExitDialog(true)) {
                    e.consume();
                }
            });
        });
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
        if (App.getActiveTest() == null || closeTest()) {
            App.setLoggedEtudiant(null);
            App.gotoLogin();
        }
    }

    public void openTest(int idTest) {
        try {
            App.setActiveTest(App.getEmitter().getTest(idTest));
            Stage testStage = new Stage();
            testStage.initOwner(App.getStage());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Test.fxml"));
            Parent root;
            root = fxmlLoader.load();
            testController = fxmlLoader.getController();
            Scene scene = new Scene(root, 1024, 720);
            scene.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
            testStage.setScene(scene);
            testStage.setTitle("Online Tests");
            testStage.setResizable(false);
            testStage.show();
        } catch (Exception e) {
            App.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean closeTest() {
        return testController.showSaveAndExitDialog(true);
    }


}
