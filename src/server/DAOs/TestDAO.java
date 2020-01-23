package server.DAOs;

import javafx.scene.chart.PieChart;
import javafx.scene.control.TextField;
import models.*;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TestDAO {

    private Connection conn;

    public TestDAO(){
        conn = DataSource.getInstance().getConnection();
    }

    public ArrayList<Test> getTests(String cne){
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "select * from tests where id_test in(" +
                            "select a.id_test from etudiants e,affectations a where e.cne=? and a.id_groupe=e.id_groupe" +
                            "); "
                    );
            statement.setString(1,cne);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Test> tests = new ArrayList<>();
            while (resultSet.next()){
                Test test = new Test();
                test.setId(resultSet.getInt("id_test"));
                test.setMatriculeProf(resultSet.getString("matricule"));
                test.setNomProf(getNomeProfBydId(resultSet.getString("matricule")));
                test.setTitre(resultSet.getString("titre"));
                test.setLocked(resultSet.getBoolean("locked"));
                test.setDuration(resultSet.getInt("duration"));
                tests.add(test);
            }
            return tests;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }


    public ArrayList<Fiche> getFiches(String cne){
        try{
            PreparedStatement statement = conn.prepareStatement("select * from fiches where cne=?;");
            statement.setString(1,cne);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Fiche> fiches = new ArrayList<>();
            while (resultSet.next()){
                Fiche fiche = new Fiche();
                fiche.setId(resultSet.getInt("id_fiche"));
                fiche.setNomEtudiant(getNomEtudiantById(cne));
                fiche.setNomGroupeEtudiant(getNomGroupeEtudiant(cne));
                fiche.setNote(resultSet.getFloat("note"));
                fiche.setTest(getTestById(resultSet.getInt("id_test")));
                fiches.add(fiche);
            }
            return fiches;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Test getFullTestById(int id_test){
        Test test = getTestById(id_test);
        test.setQuestions(getQuestionsByTestId(id_test));
        test.setGroupes(getGroupesByTestId(id_test));
        return test;
    }


    public Test getTestById(int id_test){
        try {
            PreparedStatement statement = conn.prepareStatement("select * from tests where id_test=?;");
            statement.setInt(1,id_test);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                Test test = new Test();
                test.setId(resultSet.getInt("id_test"));
                test.setMatriculeProf(resultSet.getString("matricule"));
                test.setNomProf(getNomeProfBydId(resultSet.getString("matricule")));
                test.setTitre(resultSet.getString("titre"));
                test.setLocked(resultSet.getBoolean("locked"));
                test.setDuration(resultSet.getInt("duration"));
                return test;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //not finished yet
   /* public void submitFiche(Fiche fiche){
        try{
            ArrayList<Reponse> reponses = fiche.getReponse();
            for(Reponse reponse:reponses){
                submitReponse(reponse);
            }
            PreparedStatement statement = conn.prepareStatement(
                    "insert into fiches(id_test, cne, note) values(?,?,?)"
            );
            statement.setInt(1,fiche.getTest().getId());
            statement.setString(2,fiche.ge);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }*/

    public void submitReponse(Reponse reponse){
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "insert into reponses(id_fiche, id_question,value) values(?,?,?)"
            );
            statement.setInt(1,reponse.getIdFiche());
            statement.setInt(2,reponse.getIdQuestion());
            statement.setString(3,reponse.getValue());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNomeProfBydId(String matricule){
        try {
            PreparedStatement statement = conn.prepareStatement("select * from professeurs where matricule=?;");
            statement.setString(1,matricule);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("prenom")+" "+resultSet.getString("nom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getNomEtudiantById(String cne){
        try {
            PreparedStatement statement = conn.prepareStatement("select * from etudiants where cne = ?;");
            statement.setString(1,cne);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("prenom")+" "+resultSet.getString("nom");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public String getNomGroupeEtudiant(String cne){
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "select * from groupes g,etudiants e where g.id_groupe=e.id_groupe and cne = ?;"
            );
            statement.setString(1,cne);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("nom");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Question> getQuestionsByTestId(int id_test){
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "select * from questions where id_test=?;"
            );
            statement.setInt(1,id_test);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Question> questions = new ArrayList<>();
            while(resultSet.next()) {
                Question question = new Question();
                question.setId(resultSet.getInt("id_question"));
                question.setIdTest(resultSet.getInt("id_test"));
                question.setTexte(resultSet.getString("texte"));
                question.setValue(resultSet.getString("value"));
                questions.add(question);
            }
            return questions;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Groupe> getGroupesByTestId(int id_test){
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "select * from groupes g,affectations a where g.id_groupe=a.id_groupe and a.id_test=? ;"
            );
            statement.setInt(1,id_test);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Groupe> groupes = new ArrayList<>();
            while(resultSet.next()) {
                Groupe groupe = new Groupe();
                groupe.setId(resultSet.getInt("id_groupe"));
                groupe.setNom(resultSet.getString("nom"));
                groupes.add(groupe);
            }
            return groupes;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
