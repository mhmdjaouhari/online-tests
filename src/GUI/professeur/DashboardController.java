package GUI.professeur;

import GUI.Common;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

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
        App.setLoggedProfesseur(null);
        App.gotoLogin();
    }

    public AnchorPane getContentPane() {
        return contentPane;
    }

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }
}
