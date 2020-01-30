package server.DAOs;

import javafx.util.Pair;
import models.Fiche;
import models.Groupe;
import models.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class StatisticsDAO {
    private static Connection conn = DataSource.getInstance().getConnection();

    public static ArrayList<Pair<String,Float>> getGroupesMoyennes() throws SQLException {
        ArrayList<Pair<String,Float>> result = new ArrayList<>();
        ArrayList<Groupe> groupes = TestDAO.getAllGroupes();
        for(Groupe groupe:groupes){
            ArrayList<Test> tests = TestDAO.getTestsGroupe(groupe.getId());
            for(Test test:tests){
                result.add(new Pair<>(groupe.getNom(),average(notesFromFiches(TestDAO.getTestFiches(test.getId())))));
            }
        }
        return result;
    }

    public static ArrayList<Pair<String,Float>> getTestsMoyennes(String matricule) throws SQLException{
        ArrayList<Pair<String,Float>> result = new ArrayList<>();
        ArrayList<Test> tests = TestDAO.getProfesseurTests(matricule);
        for(Test test:tests){
            result.add(new Pair<>(test.getTitre(),average(notesFromFiches(TestDAO.getTestFiches(test.getId())))));
        }
        return result;
    }

    public static float average(ArrayList<Float>values){
        if(values.size() == 0) return 0;
        float res = 0;
        for(Float value:values){
            res += value;
        }
        return res/values.size();
    }

    private static ArrayList<Float> notesFromFiches(ArrayList<Fiche> fiches){
        ArrayList<Float> res = new ArrayList<>();
        for(Fiche fiche:fiches){
            res.add(fiche.getNote());
        }
        return res;
    }
}
