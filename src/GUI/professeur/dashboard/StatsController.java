package GUI.professeur.dashboard;

import GUI.professeur.App;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class StatsController {

    @FXML
    private VBox groupesStatsBox;

    public void initialize() {
        loadMoyennesGroupes();
    }

    public void loadMoyennesGroupes() {
        // TODO: getGroupesMoyennes() returns an ArrayList of Pair<String, Double>, where the String is the name of the groupe, and the Double is the moyenne

        // dummy data
        ArrayList<Pair<String, Double>> list = new ArrayList<>();
        list.add(new Pair<>("GINF2", 13.5));
        list.add(new Pair<>("GINF1", 12.7));
        list.add(new Pair<>("GINF3", 13.1));
        list.add(new Pair<>("GIL1", 13.9));
        list.add(new Pair<>("GIL2", 12.1));
        list.add(new Pair<>("GIL3", 20.0));

        //Defining the y axis
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Moyenne des notes");

        //Defining the x axis
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Groupes");

        XYChart.Series<String, Double> moyenneSeries = new XYChart.Series<>();
        for (Pair<String, Double> pair : list) {
            moyenneSeries.getData().add(new XYChart.Data<>(pair.getKey(), pair.getValue()));
        }

        BarChart groupesMoyennesChart = new BarChart(xAxis, yAxis);
        groupesMoyennesChart.getData().add(moyenneSeries);
        groupesMoyennesChart.setLegendVisible(false);

        groupesStatsBox.getChildren().add(groupesMoyennesChart);
    }

}
