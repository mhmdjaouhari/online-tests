package server.DAOs;

import javafx.collections.ObservableList;
import javafx.util.Pair;
import models.Etudiant;
import models.Fiche;
import models.Groupe;
import models.Test;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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

    // get the number of std in each group
    public static HashMap<String,Integer> getCountEtudIngroup(){
        Statement st= null;
        HashMap<String,Integer> ArrCount=null;
        Integer nbr;
        try {
            ArrayList<Groupe> grps=TestDAO.getAllGroupes();
            ArrCount=new HashMap<String,Integer>();
            for (Groupe gp: grps)
            {
                nbr=EtudiantDAO.getAllEtudiants((int)gp.getId()).size();
                if ( nbr!=null )
                ArrCount.put(gp.getNom(),nbr);
                else
                    ArrCount.put(gp.getNom(),0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ArrCount;
    }

    // get the nbr of tests in each grp
    public static HashMap<String,Integer> getCountTestsIngroup(){
        Statement st= null;
        HashMap<String,Integer> ArrCount=null;
        Integer nbr;
        try {
            ArrayList<Groupe> grps=TestDAO.getAllGroupes();
            ArrCount=new HashMap<String,Integer>();
            for (Groupe gp: grps)
            {
                nbr=TestDAO.getTestsGroupe((int)gp.getId()).size();
                if ( nbr!=null )
                    ArrCount.put(gp.getNom(),nbr);
                else
                    ArrCount.put(gp.getNom(),0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ArrCount;
    }

}
