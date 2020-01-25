package GUI.professeur.dashboard;

import GUI.Common;
import GUI.professeur.App;
import GUI.professeur.GroupeTableController;
import GUI.professeur.TestFormController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import models.Etudiant;
import models.Groupe;
import models.Test;

import java.util.ArrayList;

public class EtudiantsController {

    @FXML
    private ScrollPane groupesScrollPane;

    public void initialize() {
        try {
            ArrayList<Groupe> allGroupes = App.getEmitter().getGroupes();
            VBox content = new VBox();
            content.setSpacing(8);
            content.setPadding(new Insets(8));
            for (Groupe groupe : allGroupes) {
                content.getChildren().addAll(createGroupeRow(groupe));
            }
            groupesScrollPane.setContent(content);
        } catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }

    }

    public JFXButton createGroupeRow(Groupe groupe) {
        JFXButton row = new JFXButton();
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setButtonType(JFXButton.ButtonType.RAISED);
        row.setRipplerFill(Paint.valueOf("#046dd5"));
        row.setStyle("-fx-background-color: #fff");
        row.setPrefHeight(64);
        row.setPrefWidth(640);
        Label titleLabel = new Label(groupe.getNom());
        titleLabel.setStyle("-fx-font-size: 24");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setGraphic(titleLabel);
        row.setOnAction(e -> {
            showGroupe(groupe);
        });
        return row;
    }

    private void showGroupe(Groupe groupe) {
        try {
            Stage groupeStage = new Stage();
            groupeStage.initOwner(App.getStage());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/GUI/professeur/GroupeTable.fxml"));
            Parent root;
            root = fxmlLoader.load();
            Scene scene = new Scene(root, 960, 640);
            groupeStage.setScene(scene);
            groupeStage.setTitle("Étudiants du groupe " + groupe.getNom());
            GroupeTableController controller = fxmlLoader.getController();
//            controller.loadEtudiants(App.getEmitter().getEtudiants(groupe.getId())); // TODO
            Groupe groupe1 = new Groupe();
            Etudiant e = new Etudiant();
            e.setCNE("9832698");
            e.setNom("simo");
            e.setPrenom("simohamed");
            groupe1.getEtudiants().add(e);
            groupe1.getEtudiants().add(e);
            groupe1.getEtudiants().add(e);
            controller.loadEtudiants(groupe1);
            groupeStage.show();
        } catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }
}
