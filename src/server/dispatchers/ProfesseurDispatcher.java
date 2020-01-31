package server.dispatchers;

import GUI.admin.ConsolleController;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import models.*;
import server.DAOs.EtudiantDAO;
import server.DAOs.ProfesseurDAO;
import server.DAOs.StatisticsDAO;
import server.DAOs.TestDAO;
import util.Action;
import util.Request;
import util.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProfesseurDispatcher {
    public static Response handle(Request request){
        Action action = request.getAction();
        Response response;
        try{
            switch (action) {
                case LOGIN:{
                    ArrayList<String> credentials = (ArrayList<String>) request.getData();
                    Professeur professeur = ProfesseurDAO.login(credentials.get(0),credentials.get(1));
                    response = new Response(0,"Proffesseur logged successfully",professeur);
                    ConsolleController.log.appendText("Le professeur "+professeur.getNom()+" "+professeur.getPrenom()+" est connecté au serveur"+"\n");
                    break;
                }
                case EXIT: {
                    System.out.println("Closing session...");
                    response = new Response(0, "Closing session...");
                    break;
                }
                case CREATE_PROF: {
                    Professeur professeur = (Professeur) request.getData();
                    ProfesseurDAO.addProfesseur(professeur);
                    response = new Response(0,"Professeur created successfully");
                    break;
                }
                case UPDATE_PROF: {
                    ArrayList<Professeur> ArrayProf = ( ArrayList<Professeur>) request.getData();
                    ProfesseurDAO.update( ArrayProf.get(0), ArrayProf.get(1));
                    response = new Response(0,"Proffesseur updated successfully");
                    break;
                }
                case DELETE_PROF: {
                    Professeur prof = (Professeur) request.getData();
                    ProfesseurDAO.delete(prof);
                    response = new Response(0,"Proffesseur "+prof.getNom()+" deleted successfully");
                    break;
                }
                case GET_ALL_PROFS:{
                    ObservableList<Professeur> professeurs = ProfesseurDAO.getAllProfesseurs();
                    response = new Response(0,"Professeurs loaded successfully",professeurs);
                    break;
                }
                case GET_PROF: {
                    String matricule = (String) request.getData();
                    Professeur professeur = ProfesseurDAO.getProfesseurById(matricule);
                    response = new Response(0,"professeur loadded successfully",professeur);
                    break;
                }
                case GET_TESTS_PROFESSEUR: {
                    String matricule = (String) request.getData();
                    ArrayList<Test> tests = TestDAO.getProfesseurTests(matricule);
                    response = new Response(0,"Tests loaded successfully",tests);
                    break;
                }
                case GET_TEST: {
                    int id_test = (int) request.getData();
                    Test test = TestDAO.getTestById(id_test);
                    response = new Response(0, "Test loaded successfully", test);
                    break;
                }
                case GET_NEW_TESTS_ETUDIANT: {
                    String cne = (String) request.getData();
                    ArrayList<Test> etudiantTests = TestDAO.getEtudiantTests(cne,true);
                    String message = etudiantTests.size() + " Tests loaded successfully";
                    response = new Response(0, message, etudiantTests);
                    break;
                }
                case GET_OLD_TESTS_ETUDIANT: {
                    String cne = (String) request.getData();
                    ArrayList<Test> etudiantTests = TestDAO.getEtudiantTests(cne,false);
                    String message = etudiantTests.size() + " Tests loaded successfully";
                    response = new Response(0, message, etudiantTests);
                    break;
                }
                case GET_ALL_TESTS_ETUDIANT: {
                    String cne = (String) request.getData();
                    ArrayList<Test> etudiantTests = TestDAO.getEtudiantTests(cne,null);
                    String message = etudiantTests.size() + " Tests loaded successfully";
                    response = new Response(0, message, etudiantTests);
                    break;
                }
                case GET_FICHES_ETUDIANT: {
                    String cne = (String) request.getData();
                    ArrayList<Fiche> fichesEtudiants = TestDAO.getEtudiantFiches(cne);
                    String message = fichesEtudiants.size() + " Fiches loaded successfully";
                    response = new Response(0,message,fichesEtudiants);
                    break;
                }
                case GET_FICHES_TEST: {
                    int idTest = (int) request.getData();
                    ArrayList<Fiche> fichesTests = TestDAO.getTestFiches(idTest);
                    String message = fichesTests.size() + " Fiche loaded successfully";
                    response = new Response(0,message,fichesTests);
                    break;
                }
                case ADD_TEST:{
                    Test test = (Test) request.getData();
                    TestDAO.createTest(test);
                    response = new Response(0,"Test created successfully");
                    break;
                }
                case ADD_TEST_2: {
                    ArrayList<Object> data = (ArrayList<Object>) request.getData();
                    Integer idGroupe = 0;
                    Test test = new Test();
                    for(Object elm:data){
                        if(elm instanceof Integer){
                            idGroupe = (Integer) elm;
                        }
                        else if(elm instanceof Test){
                            test = (Test) elm;
                        }
                        else{
                            throw new IOException("Invalid request data");
                        }
                    }
                    TestDAO.createTest2(test,idGroupe);
                    response = new Response(0,"Test added successfully");
                    break;
                }
                case DELETE_TEST:{
                    int idTest = (int)request.getData();
                    TestDAO.deleteTest(idTest);
                    response = new Response(0,"Test deleted successfully");
                    break;
                }
                case UPDATE_TEST:{
                    ArrayList<Object> data = (ArrayList<Object>) request.getData();
                    Integer oldTestId = 0;
                    Test newTest = new Test();
                    for(Object elm:data){
                        if(elm instanceof Integer){
                            oldTestId = (Integer) elm;
                        }
                        else if(elm instanceof Test){
                            newTest = (Test) elm;
                        }
                        else{
                            throw new IOException("Invalid request data");
                        }
                    }
                    TestDAO.updateTest(oldTestId, newTest);
                    response = new Response(0,"Proffesseur updated successfully");
                    break;
                }
                case GET_GROUPES: {
                    ArrayList<Groupe> groupes = TestDAO.getAllGroupes();
                    response = new Response(0,"Groupes loadded successfully",groupes);
                    break;
                }
                case GET_ETUDIANTS_GROUPE:{
                    int id_groupe = (int) request.getData();
                    ArrayList<Etudiant> etudiants = EtudiantDAO.getAllEtudiantsInGroupe(id_groupe);
                    response = new Response(0,"Etudiants loadded successfully",etudiants);
                    break;
                }
                case GET_FULL_TEST:{
                    int id_test = (int) request.getData();
                    Test test = TestDAO.getFullTestById(id_test);
                    response = new Response(0,"Test loaded successfully",test);
                    break;
                }
                case GET_MOYENNES_GROUPES:{
                    ArrayList<Pair<String,Float>> results = StatisticsDAO.getGroupesMoyennes();
                    response = new Response(0,"Moyennes loadded",results);
                    break;
                }
                case GET_TESTS_MOYENNE:{
                    String matricule = (String) request.getData();
                    ArrayList<Pair<String,Float>> results = StatisticsDAO.getTestsMoyennes(matricule);
                    response = new Response(0,"Moyennes loadded",results);
                    break;
                }
                case PUBLISH_TEST_RESULTS:{
                    int id_test = (int) request.getData();
                    TestDAO.publishTestResults(id_test);
                    response = new Response(0,"Test published successfully");
                    break;
                }
                default: {
                    System.out.println(action+" Action not found");
                    response = new Response(0, "Action not found");
                }
            }
        }catch (SQLException | IOException e){
            response = new Response(1,e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}
