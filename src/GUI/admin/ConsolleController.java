package GUI.admin;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import server.Server;

public class ConsolleController {

    @FXML
    JFXButton bntStart,btnStop,btnClear;

    @FXML
    public TextArea log;

    Thread starter;

    public void startServer(ActionEvent actionEvent) {
        // create thread to start the server
        starter= new Thread(new Server());
        starter.start();
        ///
        log.appendText("Server started ...\n");
        bntStart.setDisable(true);
        btnStop.setDisable(false);
        System.out.println("clicked");
        bntStart.setText("Server On...");
    }

    // here is some problems with threads and process
    public void stopServer(ActionEvent actionEvent) {
        // stop the thread server
        Server.setStopServer(false);
        starter.interrupt();
        ///
        log.appendText("Server stopping... \n");
        bntStart.setDisable(false);
        btnStop.setDisable(true);
        System.out.println("Clicked");
        bntStart.setText("Start Server");
    }

    public void ClearLog(ActionEvent actionEvent) {
        log.clear();
    }

}
