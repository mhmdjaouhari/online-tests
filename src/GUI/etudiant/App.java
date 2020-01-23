package GUI.etudiant;

import client.Client;
import client.actionEmitters.EtudiantActionEmitter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
            showErrorAlert("Server is offline");
            throw new Exception("Server is offline");
        }
        emitter = (EtudiantActionEmitter) client.getEmitter();
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Login.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Online Tests");
        Scene scene = new Scene(root, 240, 480);
        scene.getStylesheets().add(getClass().getResource("/GUI/gui.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
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
            replaceSceneContent("Login.fxml", 240, 480);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void gotoDashboard() {
        try {
            replaceSceneContent("Dashboard.fxml", 960, 640);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static FXMLLoader replaceSceneContent(String fxml, int v, int v1) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(App.class.getResource(fxml));
        Parent page = fxmlLoader.load();
        Scene scene = stage.getScene();

//        if (scene != null) {
        //            stage.getScene().setRoot(page);
        stage.setWidth(v);
        stage.setHeight(v1 + 37);
//        } else {
        scene = new Scene(page, v, v1);
        System.out.println("SCENE 1 " + scene.getWidth() + " " + scene.getHeight());
        scene.getStylesheets().add(App.class.getResource("/GUI/gui.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.sizeToScene();
        stage.setResizable(false);
//        }
        stage.centerOnScreen();
        return fxmlLoader;
    }

    public static void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
