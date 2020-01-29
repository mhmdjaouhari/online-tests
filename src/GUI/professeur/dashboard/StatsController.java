package GUI.professeur.dashboard;

import GUI.Common;
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
    @FXML
    private VBox testsStatsBox;

    public void initialize() {
        loadMoyennesGroupes();
        loadMoyennesTests();
    }

    public void loadMoyennesGroupes() {
        ArrayList<Pair<String, Float>> list = new ArrayList<>();
        try {
            list = App.getEmitter().getGroupesMoyennes();
        } catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Moyenne des notes");
        yAxis.setAutoRanging(false);
        yAxis.setUpperBound(20);
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Groupes");
        XYChart.Series<String, Float> moyenneSeries = new XYChart.Series<>();
        for (Pair<String, Float> pair : list) {
            moyenneSeries.getData().add(new XYChart.Data<>(pair.getKey(), pair.getValue()));
        }
        BarChart groupesMoyennesChart = new BarChart(xAxis, yAxis);
        groupesMoyennesChart.setMinWidth(list.size() * 96);
        groupesMoyennesChart.setMaxWidth(list.size() * 96);
        groupesMoyennesChart.getData().add(moyenneSeries);
        groupesMoyennesChart.setLegendVisible(false);
        groupesStatsBox.getChildren().add(groupesMoyennesChart);
    }

    public void loadMoyennesTests() {
        ArrayList<Pair<String, Float>> list = new ArrayList<>();
        try {
            list = App.getEmitter().getTestsMoyennes(App.getLoggedProfesseur().getMatricule());
        } catch (Exception e) {
            Common.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Moyenne des notes");
        yAxis.setAutoRanging(false);
        yAxis.setUpperBound(20);
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Tests");
        XYChart.Series<String, Float> moyenneSeries = new XYChart.Series<>();
        for (Pair<String, Float> pair : list) {
            moyenneSeries.getData().add(new XYChart.Data<>(pair.getKey(), pair.getValue()));
        }
        BarChart testsMoyennesChart = new BarChart(xAxis, yAxis);
        testsMoyennesChart.setMinWidth(list.size() * 96);
        testsMoyennesChart.setMaxWidth(list.size() * 96);


        testsMoyennesChart.getData().add(moyenneSeries);
        testsMoyennesChart.setLegendVisible(false);
        testsStatsBox.getChildren().add(testsMoyennesChart);
    }


}
