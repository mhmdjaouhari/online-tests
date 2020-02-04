package GUI.professeur;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
            switchToTab("dashboard/Stats.fxml", statsButton);
        });
        switchToTab("dashboard/Tests.fxml", testsButton);

        Image fileIcon = new Image(getClass().getResourceAsStream("icon-file.png"));
        ImageView fileIconView = new ImageView(fileIcon);
        testsButton.setGraphic(fileIconView);

        Image personIcon = new Image(getClass().getResourceAsStream("icon-person.png"));
        ImageView personIconView = new ImageView(personIcon);
        etudiantsButton.setGraphic(personIconView);

        Image barChartIcon = new Image(getClass().getResourceAsStream("icon-bar-chart.png"));
        ImageView barChartIconView = new ImageView(barChartIcon);
        statsButton.setGraphic(barChartIconView);

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

}
