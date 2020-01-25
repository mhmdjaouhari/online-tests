package server.DAOs;

import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class TestDAO {

    private static Connection conn;

    public TestDAO(){
        conn = DataSource.getInstance().getConnection();
    }


    //get tests that a etudiant have to pass
    public static ArrayList<Test> getEtudiantTests(String cne) throws SQLException{
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

    }

    //get tests created by a given prof
    public static ArrayList<Test> getProfesseurTests(String matricule) throws SQLException{
        PreparedStatement statement = conn.prepareStatement(
                "select * from tests where matricule = ?"
        );
        statement.setString(1,matricule);
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

    }

    //get fiches belong to an etudiant
    public static ArrayList<Fiche> getEtudiantFiches(String cne) throws SQLException {
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

    }

    //get fiches belong to a test
    public static ArrayList<Fiche> getTestFiches(int id_test) throws SQLException{
        PreparedStatement statement = conn.prepareStatement("select * from fiches where id_test=?;");
        statement.setInt(1,id_test);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Fiche> fiches = new ArrayList<>();
        while (resultSet.next()){
            Fiche fiche = new Fiche();
            fiche.setId(resultSet.getInt("id_fiche"));
            fiche.setNomEtudiant(getNomEtudiantById(resultSet.getString("cne")));
            fiche.setNomGroupeEtudiant(getNomGroupeEtudiant(resultSet.getString("cne")));
            fiche.setNote(resultSet.getFloat("note"));
            fiche.setTest(getTestById(id_test));
            fiches.add(fiche);
        }
        return fiches;

    }

    //get test with questions by ID
    public static Test getFullTestById(int id_test) throws SQLException{
        Test test = getTestById(id_test);
        test.setQuestions(getQuestionsByTestId(id_test));
        test.setGroupes(getGroupesByTestId(id_test));
        return test;
    }

    //get Test by ID
    public static Test getTestById(int id_test) throws SQLException{
        PreparedStatement statement = conn.prepareStatement("select * from tests where id_test=?;");
        statement.setInt(1,id_test);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()) {
            Test test = new Test();
            test.setId(resultSet.getInt("id_test"));
            test.setMatriculeProf(resultSet.getString("matricule"));
            test.setNomProf(getNomeProfBydId(resultSet.getString("matricule")));
            test.setTitre(resultSet.getString("titre"));
            test.setLocked(resultSet.getInt("locked") == 1);
            test.setDuration(resultSet.getInt("duration"));
            test.setPenalite(resultSet.getInt("penalite") == 1);
            return test;
        }else {
            throw new SQLException("Test not found");
        }
    }


    public static int submitFiche(Fiche fiche) throws SQLException{
        if(fiche.getReponse() == null){
            throw new SQLException("Reponse Object is Null");
        }
        PreparedStatement statement = conn.prepareStatement("INSERT into fiches(id_test, cne) values(?,?)",Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1,fiche.getTest().getId());
        statement.setString(2,fiche.getCNE());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        int id_fiche;
        if (resultSet.next()){
            id_fiche = resultSet.getInt(1);
        }else {
            throw new SQLException();
        }
        fiche.setId(id_fiche);
        ArrayList<Reponse> reponses = fiche.getReponse();
        float note = calculeNote(fiche);
        for(Reponse reponse:reponses){
            submitReponse(reponse);
        }
        System.out.println("id fiche : "+id_fiche+" note : "+note);
        PreparedStatement statement1 = conn.prepareStatement("update fiches set note=? where id_fiche=?");
        statement1.setFloat(1,note);
        statement1.setInt(2,id_fiche);
        if(statement1.executeUpdate() == 0){
            throw new SQLException();
        }
        System.out.println("Fiche submitted");
        return id_fiche;
    }

    public static float calculeNote(Fiche fiche) throws SQLException{
        if(fiche.getTest() == null){
            throw new SQLException("Test object is Null");
        }
        //check if the fiche exist
        PreparedStatement statement = conn.prepareStatement("select * from fiches where id_test = ? and id_fiche=?;");
        statement.setInt(1,fiche.getTest().getId());
        statement.setInt(2,fiche.getId());
        ResultSet resultSet = statement.executeQuery();
        if(!resultSet.next()){
            return -1;
        }
        //completing test object
        fiche.setTest(getTestById(fiche.getTest().getId()));
        ArrayList<Reponse> reponses;
        if(fiche.getReponse() != null){
            reponses = fiche.getReponse();
        }else{
            reponses = getReponsesOfFiche(fiche.getId());
        }
        int numberOfCorrectAnswers = 0 ;
        int score=0;
        assert reponses != null;
        for(Reponse reponse:reponses){
            ArrayList<String> values = new ArrayList<>();
            Collections.addAll(values,reponse.getValue().split(",", 0));
            Question question = getQuestionById(reponse.getIdQuestion());
            ArrayList<String> correctValues = new ArrayList<>();
            Collections.addAll(correctValues,question.getValue().split(",",0));
            if(fiche.getTest().isPenalite()){
                //Compute number of correct answer for canadian system
                numberOfCorrectAnswers += correctValues.size();
                for(String value:values){
                    if(correctValues.contains(value)){
                        score++;
                    }else{
                        score--;
                    }
                }
            }else{
                boolean correct = true;
                numberOfCorrectAnswers++;
                for(String correctValue:correctValues){
                    if(!values.contains(correctValue)){
                        correct = false;
                        break;
                    }
                }
                if(correct){
                    score++;
                }
            }
        }
        //System.out.println("score : "+score+" number of correct answers :"+numberOfCorrectAnswers);
        if(score <= 0) return 0;
        float note = ((float)score/numberOfCorrectAnswers)*20;
        return note;
    }


    //private util  to submit fiche (submit individual response)
    private static void submitReponse(Reponse reponse)throws SQLException{
        PreparedStatement statement = conn.prepareStatement(
                "insert into reponses(id_fiche, id_question,value) values(?,?,?)"
        );
        statement.setInt(1,reponse.getIdFiche());
        statement.setInt(2,reponse.getIdQuestion());
        statement.setString(3,reponse.getValue());
        statement.executeUpdate();

    }


    //get question by ID
    public static Question getQuestionById(int id_question) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("select * from questions where id_question=?");
        statement.setInt(1,id_question);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            Question question = new Question();
            question.setValue(resultSet.getString("value"));
            question.setId(resultSet.getInt("id_question"));
            question.setTexte(resultSet.getString("texte"));
            question.setIdTest(resultSet.getInt("id_test"));
            return question;
        }
        else {
            throw new SQLException("Question not found");
        }

    }

    // get answers of a given fiche by id
    public static ArrayList<Reponse> getReponsesOfFiche(int id_fiche) throws SQLException{
        PreparedStatement statement = conn.prepareStatement("select * from reponses where id_fiche=?");
        statement.setInt(1,id_fiche);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Reponse> reponses = new ArrayList<>();
        while(resultSet.next()) {
            Reponse reponse = new Reponse();
            reponse.setId(resultSet.getInt("id_reponse"));
            reponse.setIdFiche(resultSet.getInt("id_fiche"));
            reponse.setIdQuestion(resultSet.getInt("id_question"));
            reponse.setValue(resultSet.getString("value"));
            reponses.add(reponse);
        }
        return reponses;
    }

    //get prenom & nom of a given prof by matricule
    public static String getNomeProfBydId(String matricule) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("select * from professeurs where matricule=?;");
        statement.setString(1,matricule);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            return resultSet.getString("prenom")+" "+resultSet.getString("nom");
        }
        throw new SQLException("Proffesseur not found");
    }

    //get prenom & nom of a given etudiant by CNE
    public static String getNomEtudiantById(String cne) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("select * from etudiants where cne = ?;");
        statement.setString(1,cne);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()) {
            return resultSet.getString("prenom") + " " + resultSet.getString("nom");
        }
        throw new SQLException("Proffesseur not found");
    }

    //get nom of groupe that a given student belong by CNE
    public  static String getNomGroupeEtudiant(String cne) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "select * from groupes g,etudiants e where g.id_groupe=e.id_groupe and cne = ?;"
        );
        statement.setString(1,cne);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            return resultSet.getString("nom");
        }
        throw new SQLException("Etudiant not found or not affected");
    }

    //get questions of a test
    public static ArrayList<Question> getQuestionsByTestId(int id_test) throws SQLException {
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

    }

    //get groupes that a test belong to by id test
    public static ArrayList<Groupe> getGroupesByTestId(int id_test) throws SQLException {
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

    }

    //get Groupe by id
    public static Groupe getGroupeById(int id_groupe) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("select * from groupes where id_groupe=?;");
        statement.setInt(1,id_groupe);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            Groupe groupe = new Groupe();
            groupe.setNom(resultSet.getString("nom"));
            groupe.setId(resultSet.getInt("id_groupe"));
            return groupe;
        }
        throw new SQLException("Groupe not found");
    }

    //get Full Groupe by id
    public static Groupe getFullGroupeById(int id_groupe) throws SQLException {
        Groupe groupe = getGroupeById(id_groupe);
        ArrayList<Etudiant> etudiants = EtudiantDAO.getAllEtudiantsInGroupe(id_groupe);
        groupe.setEtudiants(etudiants);
        return groupe;
    }


    //get all existed groupes
    public static ArrayList<Groupe> getAllGroupes() throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "select * from groupes;"
        );
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Groupe> groupes = new ArrayList<>();
        while(resultSet.next()) {
            Groupe groupe = new Groupe();
            groupe.setId(resultSet.getInt("id_groupe"));
            groupe.setNom(resultSet.getString("nom"));
            groupes.add(groupe);
        }
        return groupes;
    }


    /*========================== CREATION ==========================*/

    //Add a test to a specific groupe
    public static void addTest(Test test,int id_groupe) throws SQLException {
        PreparedStatement statement =conn.prepareStatement(
                "insert into Test(matricule,titre,duration,locked) values(?,?,?,?);"
        ,Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, test.getMatriculeProf());
        statement.setString(2, test.getTitre());
        statement.setInt(3,test.getDuration());
        statement.setInt(4,test.isLocked()?1:0);
        ResultSet resultSet = statement.getGeneratedKeys();
        if(!resultSet.next()){
            throw new SQLException("Problem in adding test");
        }
        statement =conn.prepareStatement("insert into affectations(id_test,id_groupe) values(?,?);");
        statement.setInt(1, resultSet.getInt(1));
        statement.setInt(2, id_groupe);
        if(statement.executeUpdate() == 0){
            throw new SQLException("Problem in adding test to the groupe");
        }

    }

    //Add question to a test
    public static void addQuestion(Question question) throws SQLException {
        int id_test = question.getIdTest();
        PreparedStatement statement = conn.prepareStatement("select * from tests where id_test = ?;");
        statement.setInt(1,id_test);
        ResultSet resultSet = statement.executeQuery();
        if(!resultSet.next()){
            throw new SQLException("Invalid test id");
        }
        statement =conn.prepareStatement("insert into questions(id_test,texte,value) values(?,?,?);");
        statement.setInt(1, question.getIdTest());
        statement.setString(2, question.getTexte());
        statement.setString(3, question.getValue());
        if(statement.executeUpdate() == 0){
            throw new SQLException("Problem when adding question");
        }
    }
    
    /*=========================== UPDATE ========================= */
    public static void updateTest(int oldTestId,Test newTest) throws SQLException {
        Test oldTest = getTestById(oldTestId);
        PreparedStatement statement = conn.prepareStatement(
                "update tests set matricule = ?,titre=?,duration=?,locked=?,penalite=? where id_test=?;"
        );
        if(newTest.getMatriculeProf() != null){
            //check if prof exist
            ProfesseurDAO.getProfesseurById(newTest.getMatriculeProf());
            statement.setString(1,newTest.getMatriculeProf());
        }else{
            statement.setString(1,oldTest.getMatriculeProf());
        }
        if(newTest.getTitre() != null){
            statement.setString(2,newTest.getTitre());
        }else {
            statement.setString(2,newTest.getTitre());
        }

        if(newTest.getDuration() != 0){
            statement.setInt(3,newTest.getDuration());
        }else {
            statement.setInt(3,newTest.getDuration());
        }

        if(newTest.isLocked() != null){
            statement.setInt(4,newTest.isLocked()?1:0);
        }else {
            statement.setInt(4,newTest.isLocked()?1:0);
        }

        if(newTest.isPenalite() != null){
            statement.setInt(5,newTest.isPenalite()?1:0);
        }else {
            statement.setInt(5,newTest.isPenalite()?1:0);
        }
        statement.setInt(6,oldTestId);
        if(statement.executeUpdate() == 0) {
            throw new SQLException("Test doesn't exist");
        }
    }

    /* ====================== Delete Test ======================== */
    public static void deleteTest(int id_test) throws SQLException{
        PreparedStatement statement = conn.prepareStatement("delete from tests where id_test=?");
        statement.setInt(1,id_test);
        if(statement.executeUpdate() == 0){
            throw new SQLException("Test doesn't exist");
        }
    }

    //USELESS OF NOW
    private static HashMap<String,Integer> getFrequencies(String[] s){
        HashMap<String,Integer> freq = new HashMap<>();
        for(int i=0;i<s.length;i++){
            freq.put(s[0],freq.get(s[0])+1);
        }
        return freq;
    }

}
