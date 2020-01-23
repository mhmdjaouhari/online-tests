package GUI.admin;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;


public class DashBoardController implements Initializable {

    @FXML
    private AnchorPane holderPane;
    @FXML
    private JFXButton btnStatistics;
    @FXML
    private JFXButton btnConsole;
    @FXML
    private JFXButton btnEtudiants;
    @FXML
    private JFXButton btnProfesseurs;
    @FXML
    private JFXButton btnProfile;

    AnchorPane Console,Etudiants,Professeurs,Profile,Statistics;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Console = FXMLLoader.load(getClass().getResource("Console.fxml"));
            Etudiants = FXMLLoader.load(getClass().getResource("Etudiants.fxml"));
            Professeurs = FXMLLoader.load(getClass().getResource("Professeurs.fxml"));
            Profile = FXMLLoader.load(getClass().getResource("Profile.fxml"));
            Statistics = FXMLLoader.load(getClass().getResource("Statistics.fxml"));
            setNode(Console);
        } catch (IOException e) {
                e.printStackTrace();
        }
    }


    //Set selected node to a content holder
    private void setNode(Node node) {

        holderPane.getChildren().clear();
        holderPane.getChildren().add((Node) node);
        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }


    public void switchProfile(ActionEvent actionEvent) {
        setNode(Profile);
    }


    public void switchEtudiants(ActionEvent actionEvent) {
        setNode(Etudiants);
    }

    public void switchProfesseurs(ActionEvent actionEvent) {
        setNode(Professeurs);
    }

    public void switchStatistics(ActionEvent actionEvent) {
        setNode(Statistics);
    }

    public void switchConsole(ActionEvent actionEvent) {
        setNode(Console);
    }

}
