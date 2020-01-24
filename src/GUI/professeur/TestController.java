package GUI.professeur;

import GUI.professeur.App;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestController {

    private Stage stage;


    public void initialize() {

    }

    public boolean showSaveAndExitDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(stage);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        dialog.setResizable(true);
        dialog.getDialogPane().setPrefSize(320, 160);

        dialog.setTitle("Quitter la création du test");
        dialog.setContentText("Voulez-vous enregistrer avant de quitter ?");
        AtomicBoolean returnValue = new AtomicBoolean(false);
        dialog.setOnCloseRequest(e -> {
            ButtonType result = dialog.getResult();
            if (result == ButtonType.CANCEL) {
                e.consume();
            } else {
                if (result == ButtonType.YES) {
                    //SAVE TEST
                }
                returnValue.set(true);
                App.setActiveTest(null);
                stage.close();
            }

        });
        dialog.showAndWait();
        return returnValue.get();
    }


}
