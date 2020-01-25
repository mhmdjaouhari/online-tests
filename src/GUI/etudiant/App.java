package GUI.etudiant;

import GUI.Common;
import client.Client;
import client.actionEmitters.EtudiantActionEmitter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Etudiant;
import models.Test;
import util.Role;

public class App extends Application {

    private static Stage stage;
    private static Etudiant loggedEtudiant;
    private static Test activeTest;
    private static EtudiantActionEmitter emitter;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Client client = new Client(Role.ETUDIANT);
        if (!client.connect()) {
            Common.showErrorAlert("Server is offline");
            throw new Exception("Server is offline");
        }
        emitter = (EtudiantActionEmitter) client.getEmitter();
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Login.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Online Tests");
        Scene scene = new Scene(root, 240, 480);
        primaryStage.setMinWidth(240);
        primaryStage.setMinHeight(480 + 37);
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

    public static EtudiantActionEmitter getEmitter() {
        return emitter;
    }

    public static Etudiant getLoggedEtudiant() {
        return loggedEtudiant;
    }

    public static void setLoggedEtudiant(Etudiant etudiant) {
        loggedEtudiant = etudiant;
    }

    public static Test getActiveTest() {
        return activeTest;
    }

    public static void setActiveTest(Test test) {
        activeTest = test;
    }

    public static void gotoLogin() {
        try {
            Common.replaceSceneContent(stage, App.class.getResource("Login.fxml"), 240, 480);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void gotoDashboard() {
        try {
            Common.replaceSceneContent(stage, App.class.getResource("Dashboard.fxml"), 960, 480);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
