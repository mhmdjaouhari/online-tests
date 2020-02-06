package GUI.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import models.Groupe;
import server.DAOs.StatisticsDAO;
import server.dispatchers.ProfesseurDispatcher;
import util.Action;
import util.Request;
import util.Response;
import util.Role;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {

    @FXML BarChart<String,Integer> grpChart;
    @FXML BarChart<String,Integer> grpChart1;
    @FXML VBox nbrEtudLabel;
    @FXML VBox nbrProfLabel;


    HashMap<String,Integer> MapGrpsNamesCount;
    HashMap<String,Integer> MapGrpsNamesTests;
    public static int nbrEtd=0;
    public static int nbrPf=0;
    public static TextField OnlineProfs;
    public static TextField OnlineStudents;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        OnlineProfs=new TextField();
        OnlineStudents=new TextField();

        MapGrpsNamesCount=getCountGroups();
        MapGrpsNamesTests=getCountTestsInGroup();

        XYChart.Series set1=new XYChart.Series();
        XYChart.Series set2=new XYChart.Series();

        for(int i=0;i<MapGrpsNamesCount.size();i++)
        {
            set1.getData().add(new XYChart.Data(MapGrpsNamesCount.keySet().toArray()[i],MapGrpsNamesCount.values().toArray()[i]));
            set2.getData().add(new XYChart.Data(MapGrpsNamesTests.keySet().toArray()[i],MapGrpsNamesTests.values().toArray()[i]));
        }
        grpChart.getData().add(set1);
        grpChart1.getData().add(set2);


        OnlineProfs.setStyle("-fx-background-color: #FFC66C;");
        OnlineProfs.setFont(new Font("Ebrima Bold",25));
        OnlineProfs.setEditable(false);
        OnlineProfs.setAlignment(Pos.TOP_CENTER);

        OnlineStudents.setStyle("-fx-background-color: #FFC66C; -fx-text-alignment: center");
        OnlineStudents.setFont(new Font("Ebrima Bold",25));
        OnlineStudents.setEditable(false);
        OnlineStudents.setAlignment(Pos.TOP_CENTER);

        OnlineStudents.setLayoutX(10);
        OnlineStudents.setLayoutY(10);

        OnlineProfs.setLayoutX(10);
        OnlineProfs.setLayoutY(10);
        OnlineProfs.setPrefHeight(48);
        OnlineProfs.setPrefWidth(96);

        OnlineProfs.setText("0");
        OnlineStudents.setText("0");
        nbrProfLabel.getChildren().add(OnlineProfs);
        nbrEtudLabel.getChildren().add(OnlineStudents);
    }

    // Utils

    public HashMap<String,Integer> getCountGroups(){
        HashMap<String,Integer> groups= StatisticsDAO.getCountEtudIngroup();
        return groups;
    }
    public HashMap<String,Integer> getCountTestsInGroup(){
        HashMap<String,Integer> groups= StatisticsDAO.getCountTestsIngroup();
        return groups;
    }
}
