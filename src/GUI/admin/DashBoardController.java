package GUI.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;
import models.Admin;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;


public class DashBoardController implements Initializable {

    @FXML
    private AnchorPane holderPane;
    @FXML
    private JFXButton btnWelcome;
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
    @FXML
    private VBox vbox2;

    static Admin loggeduser ;
    static Label statServer;

    AnchorPane Console,Etudiants,Professeurs,Profile,Statistics;

    public static void setLogedUser(Admin adm) {
        loggeduser=adm;
    }

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
        btnWelcome.setText("Welcome : "+loggeduser.getNom()+" !");
        statServer=new Label("Server offline !");
        statServer.prefHeight(42);
        statServer.prefWidth(161);
        statServer.setFont(new Font(14.0));
        statServer.setTextFill(Color.web("#ff0000"));
        statServer.setAlignment(Pos.BASELINE_CENTER);

        vbox2.getChildren().add(0,statServer);
        vbox2.setAlignment(Pos.CENTER);
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

    public void logout(ActionEvent actionEvent) throws IOException {
        DashBoardController.setLogedUser(null);
        App.gotoLogin();
    }
}
