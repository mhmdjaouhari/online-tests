package GUI.etudiant;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
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

    private App app;

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void initialize() {
        setApp(App.getInstance());
        ArrayList<Test> allTests = App.getEmitter().getNewTests();

        nomEtudiant.setText(app.getLoggedEtudiant().getPrenom() + " " + app.getLoggedEtudiant().getNom());
        groupeEtudiant.setText("ID Groupe : " + app.getLoggedEtudiant().getIdGroupe());
        newTestCount.setText(Integer.toString(allTests.size()));

        VBox content = new VBox();
        content.setSpacing(8);
        content.setPadding(new Insets(8));
        for (Test test : allTests) {
            content.getChildren().addAll(createTestRow(test.getTitre(), 90, "simo"));
        }
        newTestsPane.setContent(content);
    }

    public JFXButton createTestRow(String title, int duration, String nomProf) {
        JFXButton row = new JFXButton();
        row.setButtonType(JFXButton.ButtonType.RAISED);
        row.setStyle("-fx-background-color: #fff");
        row.setPrefHeight(56);
        row.setPrefWidth(320);
        VBox vBox = new VBox();
        int hours = (int) Math.floor((float) duration / 60);
        String durationString = "" + hours + "h" + (duration - hours * 60);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold");
        Label subtitleLabel = new Label("durée : " + durationString + " - par : " + nomProf);
        subtitleLabel.setStyle("-fx-font-size: 12");
        vBox.getChildren().addAll(titleLabel, subtitleLabel);
        row.setGraphic(vBox);
        return row;
    }

    public void handleLogout(){
        app.setLoggedEtudiant(null);
        app.gotoLogin();
    }



}
