package GUI.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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

    HashMap<String,Integer> MapGrpsNamesCount;
    HashMap<String,Integer> MapGrpsNamesTests;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
