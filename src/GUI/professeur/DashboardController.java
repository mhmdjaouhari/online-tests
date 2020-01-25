package GUI.professeur;

import GUI.GUI;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import models.Test;

import java.io.IOException;
import java.util.ArrayList;

public class DashboardController {

    @FXML
    private AnchorPane contentPane;
    @FXML
    private Label nomProf;
    @FXML
    private JFXButton testsButton;
    @FXML
    private JFXButton etudiantsButton;
    @FXML
    private JFXButton statsButton;

    private TestController testController;

    public void initialize() {

        nomProf.setText(App.getLoggedProfesseur().getPrenom() + " " + App.getLoggedProfesseur().getNom());
        testsButton.setOnAction(e -> {
            switchToTab("dashboard/Tests.fxml", testsButton);
        });
        etudiantsButton.setOnAction(e -> {
            switchToTab("dashboard/Etudiants.fxml", etudiantsButton);
        });
        statsButton.setOnAction(e -> {
            switchToTab("dashboard/Etudiants.fxml", statsButton);
        });
        switchToTab("dashboard/Tests.fxml", testsButton);


//
//        Platform.runLater(() -> {
//            Stage stage = (Stage) newTestsPane.getScene().getWindow();
//            stage.setOnCloseRequest(e -> {
//                if (App.getLoggedProfesseur() != null &&
//                        App.getActiveTest() != null &&
//                        testController != null &&
//                        !testController.showSaveAndExitDialog()) {
//                    e.consume();
//                }
//            });
//        });
    }

    private void switchToTab(String fxml, JFXButton relatedButton) {
        testsButton.setStyle("");
        etudiantsButton.setStyle("");
        statsButton.setStyle("");
        relatedButton.setStyle("-fx-background-color: #555");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(fxml));
            Parent page = fxmlLoader.load();
            contentPane.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void handleLogout() {
        if (App.getActiveTest() == null || closeTest()) {
            App.setLoggedProfesseur(null);
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
            scene.getStylesheets().add(getClass().getResource("/GUI/gui.css").toExternalForm());
            testStage.setScene(scene);
            testStage.setTitle("Online Tests");
            testStage.setResizable(false);
            testStage.show();
        } catch (Exception e) {
            GUI.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean closeTest() {
        return testController.showSaveAndExitDialog();
    }


    public AnchorPane getContentPane() {
        return contentPane;
    }

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }
}
