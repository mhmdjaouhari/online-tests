package GUI.professeur;

import GUI.Common;
import client.Client;
import client.actionEmitters.ProfesseurActionEmitter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Professeur;
import models.Test;
import util.Role;

public class App extends Application {

    private static Stage stage;
    private static Professeur loggedProfesseur;
    private static ProfesseurActionEmitter emitter;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Client client = new Client(Role.PROFESSEUR);
        if (!client.connect()) {
            Common.showErrorAlert("Server is offline");
            throw new Exception("Server is offline");
        }
        emitter = (ProfesseurActionEmitter) client.getEmitter();
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Login.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Online Tests");
        Scene scene = new Scene(root, 360, 560);
        primaryStage.setMinWidth(360);
        primaryStage.setMinHeight(520 + 37);
        scene.getStylesheets().add(getClass().getResource("/GUI/gui.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return stage;
    }

    public static ProfesseurActionEmitter getEmitter() {
        return emitter;
    }

    public static Professeur getLoggedProfesseur() {
        return loggedProfesseur;
    }

    public static void setLoggedProfesseur(Professeur professeur) {
        loggedProfesseur = professeur;
    }

    public static void gotoLogin() {
        try {
            Common.replaceSceneContent(stage, App.class.getResource("Login.fxml"), 360, 560);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void gotoDashboard() {
        try {
            Common.replaceSceneContent(stage, App.class.getResource("Dashboard.fxml"), 960, 560);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
