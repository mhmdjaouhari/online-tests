package GUI.etudiant;

import GUI.Common;
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
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import models.Fiche;
import models.Test;

import java.sql.SQLException;
import java.text.DecimalFormat;
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
        try {
            ArrayList<Test> newTests = null;
            ArrayList<Test> oldTests = null;
                newTests = App.getEmitter().getNewEtudiantTests(App.getLoggedEtudiant().getCNE());
                oldTests = App.getEmitter().getOldEtudiantTests(App.getLoggedEtudiant().getCNE());
            nomEtudiant.setText(App.getLoggedEtudiant().getPrenom() + " " + App.getLoggedEtudiant().getNom());
            groupeEtudiant.setText("ID Groupe : " + App.getLoggedEtudiant().getIdGroupe());
            newTestCount.setText(Integer.toString(newTests.size()));

            VBox newTestsContent = new VBox();
            newTestsContent.setSpacing(8);
            newTestsContent.setPadding(new Insets(8));

            for (Test test : newTests) {
                newTestsContent.getChildren().addAll(createNewTestRow(test));
            }
            newTestsPane.setContent(newTestsContent);

            VBox oldTestsContent = new VBox();
            oldTestsContent.setSpacing(8);
            oldTestsContent.setPadding(new Insets(8));

            for(Test test : oldTests){
                oldTestsContent.getChildren().addAll(createOldTestRow(test));
            }
            oldTestsPane.setContent(oldTestsContent);

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JFXButton createNewTestRow(Test test) {
        JFXButton row = new JFXButton();
        row.setButtonType(JFXButton.ButtonType.RAISED);
        row.setRipplerFill(Paint.valueOf("#046dd5"));
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

    public JFXButton createOldTestRow(Test test) throws Exception {
        JFXButton row = new JFXButton();
        row.setButtonType(JFXButton.ButtonType.RAISED);
        row.setRipplerFill(Paint.valueOf("#046dd5"));
        row.setStyle("-fx-background-color: #adadad");
        row.setPrefHeight(56);
        row.setPrefWidth(320);
        //row.setDisable(true);
        VBox vBox = new VBox();
        Label subtitleLabel = new Label(test.getDetails());
        Label titleLabel = new Label(test.getTitre());
        Fiche fiche = App.getEmitter().getFicheEtudiant(App.getLoggedEtudiant().getCNE(),test.getId());
        String  note = (new DecimalFormat("#.##")).format(fiche.getNote());
        Label noteLabel = new Label("Note : "+note);
        titleLabel.setStyle("-fx-font-weight: bold");
        subtitleLabel.setStyle("-fx-font-size: 12");
        vBox.getChildren().addAll(titleLabel, subtitleLabel,noteLabel);
        row.setGraphic(vBox);
        return row;
    }

    public void handleLogout() {
        App.setLoggedEtudiant(null);
        App.gotoLogin();
    }

    public void openTest(int idTest) {
        try {
            App.setActiveTest(App.getEmitter().getFullTestById(idTest));
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
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean closeTest() {
        return testController.showSaveAndExitDialog(true);
    }


}
