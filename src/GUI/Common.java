package GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;

public class Common {
    public static FXMLLoader replaceSceneContent(Stage stage, URL resource, int minWidth, int minHeight) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        Parent page = fxmlLoader.load();
        Scene scene = stage.getScene();
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight + 37);
        scene = new Scene(page);
        scene.getStylesheets().add(Common.class.getResource("gui.css").toExternalForm());
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        return fxmlLoader;
    }

    public static void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
