package GUI.admin;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import server.Server;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ConsolleController implements Initializable {

    @FXML
    JFXButton bntStart,btnStop,btnClear;
    @FXML
    AnchorPane anch;

    public static TextArea log;

    Thread starter;

    public void startServer(ActionEvent actionEvent) {
        //create thread to start the server
        Server.setStopServer(true);
        starter= new Thread(new Server());
        starter.start();
        System.out.println("kokokoko");
        //starter.interrupt();
        ///
        log.appendText("Server started ...\n");
        bntStart.setDisable(true);
        btnStop.setDisable(false);
        System.out.println("clicked");
        bntStart.setText("Server On...");
        DashBoardController.statServer.setText("Server is online !");
        DashBoardController.statServer.setTextFill(Color.web("#00ff49"));
        //System.out.println(starter.isInterrupted());
        //starter.interrupt();
    }

    // here is some problems with threads and process
    public void stopServer(ActionEvent actionEvent) throws IOException {
        Server.setStopServer(false);
        Socket socket = new Socket("localhost", 5000);
//        System.out.println(socket.isConnected());
        socket.close();
        log.appendText("Server stopping... \n");
        bntStart.setDisable(false);
        btnStop.setDisable(true);
//        System.out.println("Clickedstop");
        bntStart.setText("Start Server");
//        System.out.println(socket.isConnected());
        starter.interrupt();
//        System.out.println(starter.isInterrupted());
        DashBoardController.statServer.setTextFill(Color.web("#ff0000"));
        DashBoardController.statServer.setText("Server is Offline !");
    }

    public void ClearLog(ActionEvent actionEvent) {
        log.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log=new TextArea();
        log.setEditable(false);log.setCenterShape(false);
        log.setCache(false);log.setFocusTraversable(false);log.setLayoutX(149.0);
        log.setLayoutY(206.0);log.setPrefHeight(397);log.setPrefWidth(835);
        log.setScaleShape(false);log.setStyle("-fx-control-inner-background:#000000;-fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00;");
        log.setFont(new Font("Arial Black",14));
        anch.getChildren().add(log);
    }
}
